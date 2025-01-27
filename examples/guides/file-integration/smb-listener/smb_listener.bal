// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import wso2/ftp;
import ballerina/log;
import ballerina/io;
import ballerina/internal;
import ballerina/config;
import ballerina/http;
import wso2ftputils;

public const MOVE = "MOVE";
public const DELETE = "DELETE";
public const ERROR = "ERROR";

// Define the union type of the actions that can be performed after processing the file.
public type Operation MOVE | DELETE | ERROR;

type Config record {
    string fileNamePattern;
    string destFolder;
    string errFolder;
    string srcFolder;
    Operation opr;
};

// Define a record to get the following configurations for processing the file.
Config conf = {
    fileNamePattern: ".*.xml",
    destFolder: "/sambaOutFolder",
    errFolder: "/sambaFaultFolder",
    srcFolder: "/sambaInFolder",
    opr: MOVE
};

// Creating a smb listener instance by defining the configuration.
listener ftp:Listener remoteServer = new({
    protocol: ftp:SMB,
    host: config:getAsString("SMB_HOST"),
    port: config:getAsInt("SMB_LISTENER_PORT"),
    pollingInterval:config:getAsInt("FTP_POLLING_INTERVAL"),
    fileNamePattern:conf.fileNamePattern,  
     secureSocket: {
        basicAuth: {
            username: config:getAsString("SMB_USERNAME"),
            password: password: config:getAsString("SMB_PASSWORD")
        }
    },
    path: "/sambaInFolder"
});

// Defining the configuration of the smb client endpoint.
ftp:ClientEndpointConfig smbConfig = {
    protocol: ftp:SMB,
    host: config:getAsString("SMB_HOST"),
    port: config:getAsInt("SMB_LISTENER_PORT"),
    secureSocket: {
        basicAuth: {
            username: config:getAsString("SMB_USERNAME"),
            password: config:getAsString("SMB_PASSWORD")
        }
    }
};

// Creating a smbClient object.
ftp:Client smbClient = new(smbConfig);

service monitor on remoteServer {
    resource function fileResource(ftp:WatchEvent m) {
        foreach ftp:FileInfo v1 in m.addedFiles {

            log:printInfo("Added file path: " + v1.path);

            var proRes = processFile(untaint v1.path);

            string srcPath = createFolderPath(v1, conf.srcFolder);
            // Moving the file to another location on the same ftp server after processing.
            if (proRes == MOVE) {
                string destFilePath = createFolderPath(v1, conf.destFolder);
                error? renameErr = smbClient->rename(srcPath, destFilePath);
                log:printInfo("Moved File after processing");
            } else if (proRes == DELETE) {
                error? fileDelCreErr = smbClient->delete(srcPath);
                log:printInfo("Deleted File after processing");
            } else {
                string errFoldPath = createFolderPath(v1, conf.errFolder);
                error? processErr = smbClient->rename(srcPath, errFoldPath);
            }
        }
    }
}

// Processing logic that needs to be done on the file content based on the file type.
public function processFile(string sourcePath) returns Operation {

    string getFileName = conf.srcFolder + sourcePath;

    var getResult = smbClient->get(getFileName);

    Operation res = MOVE;

    if (getResult is io:ReadableByteChannel) {
        xml | error? jsonFileRes = wso2ftputils:readXmlFile(getResult);
        if (jsonFileRes is xml) {
            log:printInfo("File read successfully");
            if (conf.opr == MOVE) {
                res = MOVE;
            } else if (conf.opr == DELETE) {
                res = DELETE;
            }
        } else {
            log:printError("Error in reading file", err = jsonFileRes);
            res = ERROR;
        }
    } else {
        log:printError("Error in reading file.");
        res = ERROR;
    }
    return res;
}

// // Generating file paths to move the processed file.
public function createFolderPath(ftp:FileInfo v2, string folderPath) returns string {
    string p2 = createPath(v2);
    string path = folderPath + "/" + p2;
    return path;
}

public function createPath(ftp:FileInfo v3) returns string {
    int subString = v3.path.lastIndexOf("/");
    int length = v3.path.length();
    string subPath = v3.path.substring((subString + 1), length);
    return subPath;
}

