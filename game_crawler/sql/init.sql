CREATE DATABASE IF NOT EXISTS `game_crawler`

CREATE TABLE IF NOT EXISTS `t_task` (                                  
   `id` bigint(20) NOT NULL AUTO_INCREMENT ,   
   `createTime` timestamp , 
   `lastUpdateTime` timestamp ,  
   `title` varchar(300) NOT NULL,
   `jobClass` varchar(300) ,     
   `jobBeanName` varchar(300), 
   `jobDetailName` varchar(100), 
   `jobTriggerName` varchar(100), 
   `usable` int(2),
   `runnable` int(2),
   `triggerType` varchar(8),
   `targetMethod` varchar(100), 
   `cronExpression` varchar(30), 
   `jobStartTime` timestamp ,  
   `startDelay` int, 
   `repeatInterval` int,
   `description` varchar(300),
    PRIMARY KEY (`id`)                                       
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;  
ALTER TABLE t_task ADD INDEX index_task_assemble_01 (`triggerType`);
ALTER TABLE t_task ADD INDEX index_task_assemble_02 (`jobBeanName`,`jobClass`,`targetMethod`); 


CREATE TABLE t_apk(
   id BIGINT(20)  NOT NULL AUTO_INCREMENT,
   name varchar(100),
   operationWay varchar(30),  
   `type` varchar(50),  
   introduce text,  
   size varchar(10),  
   updateDate varchar(15),  
   `language` varchar(16),  
   `version` varchar(30),  
   `androidSystemVersion` varchar(50),  
   `downloadTimes` varchar(10),  
    sourceId varchar(200),
    apkUrl varchar(300),
    serverPath varchar(300),
    status tinyint(1),
    sourceName varchar(10),
    createTime timestamp,  
    lastUpdateTime timestamp,
   PRIMARY key(id)
);
alter table t_apk add index index__apk_sourceName(`sourceName`);
alter table t_apk add index index__apk_sourceId(`sourceId`);

CREATE TABLE t_picture(
    id BIGINT(20)  NOT NULL AUTO_INCREMENT,
    resourceId BIGINT(20),
	sourceUrl varchar(300),
	serverUrl varchar(300),
	pictureType varchar(20),
	sourcceId varchar(100),
	status tinyint(1),
	sourceType varchar(20),
    createTime timestamp,  
    lastUpdateTime timestamp,
   PRIMARY key(id)
);
alter table t_video add index index_video_sourceId(`sourceId`);
alter table t_picture add index index__picture_sourceType(`sourceType`);
alter table t_picture add index index__picture_pictureType(`pictureType`);





CREATE TABLE `t_video` (                                                  
           `id` int(11) NOT NULL AUTO_INCREMENT,                                   
           `videoName` varchar(50) COLLATE utf8_bin DEFAULT NULL,                  
           `videoUrl` varchar(300) COLLATE utf8_bin DEFAULT NULL,                  
           `sourceName` varchar(50) COLLATE utf8_bin DEFAULT NULL,  
           `realFileUrl` varchar(300) COLLATE utf8_bin DEFAULT NULL,
           `updateDate` varchar(30),
           `sourceId` varchar(100),
           `status` int(11) DEFAULT NULL,                                          
           `createTime` datetime DEFAULT NULL,                                     
           `lastUpdateTime` datetime DEFAULT NULL,                                 
           PRIMARY KEY (`id`)                                                      
         ) ENGINE=MyISAM AUTO_INCREMENT=686 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;  
alter table t_picture add index index__picture_sourceId(`sourceId`);
