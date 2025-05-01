CREATE TABLE IF NOT EXISTS Users(
    id INTEGER PRIMARY KEY,
    username VARCHAR(20) NOT NULL ,
    password VARCHAR(20) NOT NULL ,
    email VARCHAR(255) UNIQUE NOT NULL,
    emailVerified TIMESTAMP NULL,
    role VARCHAR(20),
    profilePicture VARCHAR(255) NULL,
    firstName VARCHAR(255) NULL,
    lastName VARCHAR(255) NULL,
    over13 BOOLEAN NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reliabilityScore INTEGER NOT NULL DEFAULT(50),
    parentId INTEGER NULL -- Foreign KEY TABLE NOT IMPLEMENTED YET

    );

CREATE TABLE IF NOT EXISTS Account
(
    id INTEGER PRIMARY KEY,
    userId INTEGER NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP,
    provider VARCHAR(255) NOT NULL,
    access_token VARCHAR(255) NULL,
    scope VARCHAR(255) NULL,
    token_type VARCHAR(255) NULL,

    CONSTRAINT fk_user_account
    FOREIGN KEY (userId)
    REFERENCES Users(id)
    ON DELETE CASCADE --Delete Account row when parent is deleted
    ON UPDATE CASCADE --Updates Account row when parent is updated

    );

CREATE TABLE IF NOT EXISTS TaskType
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS Task
(
    id INTEGER PRIMARY KEY,
    userId INTEGER NOT NULL,
    createdTime TIMESTAMP NOT NULL default(CURRENT_TIMESTAMP),
    lastAccessed TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    taskTypeId INTEGER NOT NULL,
    score FLOAT8 NULL,

    CONSTRAINT fk_task_user
    FOREIGN KEY(userId)
    REFERENCES Users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    CONSTRAINT fk_task_taskTypeId
    FOREIGN KEY(taskTypeId)
    REFERENCES TaskDataType(taskTypeId)
    ON DELETE CASCADE
    ON UPDATE CASCADE

    );
CREATE TABLE IF NOT EXISTS TaskDataType
(
    id INTEGER PRIMARY KEY,
    taskTypeId INTEGER NOT NULL,
    name VARCHAR(20),
    type VARCHAR(20),

    CONSTRAINT fk_TaskType_TaskDataType
    FOREIGN KEY(taskTypeId)
    REFERENCES TaskType(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE

    );

CREATE TABLE IF NOT EXISTS TaskData
(
    id INTEGER NOT NULL,
    taskId INTEGER NOT NULL,
    taskDataTypeId INTEGER NOT NULL,
    value VARCHAR(20) NULL,

    CONSTRAINT fk_Task_TaskData
    FOREIGN KEY(taskId)
    REFERENCES Task(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    CONSTRAINT fk_TaskDataType_TaskData
    FOREIGN KEY(taskDataTypeId)
    REFERENCES TaskDataType(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS TaskImage
(
    id INTEGER NOT NULL,
    taskId INTEGER NOT NULL,
    imageId INTEGER NOT NULL,
    extent Extent--Extent field (tuples x, y, z)

    CONSTRAINT fk_TaskImage_task
    FOREIGN KEY(taskId)
    REFERENCES Task(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    CONSTRAINT fk_Image_TaskImage
    FOREIGN KEY(imageId)
    REFERENCES Image(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE

    );

CREATE TABLE IF NOT EXISTS Image
(
    id INTEGER PRIMARY KEY ,
    cameraId INTEGER NOT NULL,
    imageURL VARCHAR(20) NOT NULL,
    captureDate TIME NOT NULL,
    captureTime TIME NOT NULL,
    roverLocation VARCHAR(20) NOT NULL,
    resolution VARCHAR(20) NOT NULL,
    pixelDensity VARCHAR(20) NOT NULL,
    rockCount INTEGER NULL,
    scouted BOOLEAN NOT NULL DEFAULT(FALSE),
    numQuadrants INTEGER NULL,
    sized BOOLEAN NULL DEFAULT(FALSE),
    priority INT NOT NULL DEFAULT(5)
    );

CREATE TYPE Extent as (
    x1 INTEGER NULL,
    y1 INTEGER NULL,
    x2 INTEGER NULL,
    y2 INTEGER NULL
    );