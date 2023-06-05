/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : zuoye_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-06-29 14:06:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_departcharge`
-- ----------------------------
DROP TABLE IF EXISTS `t_departcharge`;
CREATE TABLE `t_departcharge` (
  `chargeUserName` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  `departmentObj` varchar(20) default NULL,
  `name` varchar(20) default NULL,
  `sex` varchar(4) default NULL,
  `birthday` varchar(20) default NULL,
  `email` varchar(20) default NULL,
  PRIMARY KEY  (`chargeUserName`),
  KEY `FK656DA9D3F97E0739` (`departmentObj`),
  CONSTRAINT `FK656DA9D3F97E0739` FOREIGN KEY (`departmentObj`) REFERENCES `t_department` (`departmentNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_departcharge
-- ----------------------------
INSERT INTO `t_departcharge` VALUES ('teacher1', '123', 'BJ001', '王大治', '男', '2018-03-10', 'dazhi@163.com');
INSERT INTO `t_departcharge` VALUES ('teacher2', '123', 'BJ002', '刘达', '男', '2018-03-10', 'liuda@163.com');

-- ----------------------------
-- Table structure for `t_department`
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department` (
  `departmentNo` varchar(20) NOT NULL,
  `departmentName` varchar(20) default NULL,
  `bornDate` varchar(20) default NULL,
  PRIMARY KEY  (`departmentNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES ('BJ001', '计算机1班', '2017-03-01');
INSERT INTO `t_department` VALUES ('BJ002', '计算机2班', '2018-03-10');

-- ----------------------------
-- Table structure for `t_departtask`
-- ----------------------------
DROP TABLE IF EXISTS `t_departtask`;
CREATE TABLE `t_departtask` (
  `taskId` int(11) NOT NULL auto_increment,
  `departmentObj` varchar(20) default NULL,
  `title` varchar(60) default NULL,
  `content` longtext,
  `publishDate` varchar(20) default NULL,
  `executeState` varchar(20) default NULL,
  `evaluate` varchar(200) default NULL,
  PRIMARY KEY  (`taskId`),
  KEY `FK66B1F864F97E0739` (`departmentObj`),
  CONSTRAINT `FK66B1F864F97E0739` FOREIGN KEY (`departmentObj`) REFERENCES `t_department` (`departmentNo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_departtask
-- ----------------------------
INSERT INTO `t_departtask` VALUES ('1', 'BJ001', '开发一个人事系统', '开发部的同志们好 新的任务即将来临 马上要开发一个人事系统！', '2018-03-11', '执行中', '--');

-- ----------------------------
-- Table structure for `t_empprogress`
-- ----------------------------
DROP TABLE IF EXISTS `t_empprogress`;
CREATE TABLE `t_empprogress` (
  `progressId` int(11) NOT NULL auto_increment,
  `taskObj` int(11) default NULL,
  `progressFile` varchar(50) default NULL,
  `studentObj` varchar(20) default NULL,
  `addTime` varchar(20) default NULL,
  `evaluate` varchar(20) default NULL,
  `evalateContent` varchar(60) default NULL,
  PRIMARY KEY  (`progressId`),
  KEY `FKB9545FAAEA4E71A7` (`taskObj`),
  KEY `FKB9545FAAA2CBF239` (`studentObj`),
  KEY `FKB9545FAA5750E070` (`taskObj`),
  KEY `FKB9545FAA19D9CAD5` (`studentObj`),
  CONSTRAINT `FKB9545FAA19D9CAD5` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`),
  CONSTRAINT `FKB9545FAA5750E070` FOREIGN KEY (`taskObj`) REFERENCES `t_studenttask` (`taskId`),
  CONSTRAINT `FKB9545FAAA2CBF239` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`),
  CONSTRAINT `FKB9545FAAEA4E71A7` FOREIGN KEY (`taskObj`) REFERENCES `t_studenttask` (`taskId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_empprogress
-- ----------------------------
INSERT INTO `t_empprogress` VALUES ('1', '1', 'upload/5d2e28c5-5fb8-4fd1-8608-ece6bc2ba3f1.doc', 'STU001', '2018-03-15 15:11:16', '优', '不错哦');
INSERT INTO `t_empprogress` VALUES ('2', '2', 'upload/2f4a34ae-c06b-45fb-b501-41e61defc64c.txt', 'STU001', '2017-04-15 21:20:48', '--', '--');

-- ----------------------------
-- Table structure for `t_journal`
-- ----------------------------
DROP TABLE IF EXISTS `t_journal`;
CREATE TABLE `t_journal` (
  `journalId` int(11) NOT NULL auto_increment,
  `title` varchar(60) default NULL,
  `content` longtext,
  `journalDate` varchar(20) default NULL,
  `studentObj` varchar(20) default NULL,
  PRIMARY KEY  (`journalId`),
  KEY `FKCA2F3A4CA2CBF239` (`studentObj`),
  KEY `FKCA2F3A4C19D9CAD5` (`studentObj`),
  CONSTRAINT `FKCA2F3A4C19D9CAD5` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`),
  CONSTRAINT `FKCA2F3A4CA2CBF239` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_journal
-- ----------------------------
INSERT INTO `t_journal` VALUES ('1', '今天完成了数据库表设计', '警告需求分析 人事系统的表我员工设计了15个\r\n如下：：\r\nperson表 。。。', '2018-03-11', 'STU001');

-- ----------------------------
-- Table structure for `t_news`
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `newsId` int(11) NOT NULL auto_increment,
  `title` varchar(60) default NULL,
  `content` longtext,
  `publishTime` varchar(20) default NULL,
  PRIMARY KEY  (`newsId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_news
-- ----------------------------
INSERT INTO `t_news` VALUES ('1', '1111111', '222222222', '2018-03-11 15:16:18');

-- ----------------------------
-- Table structure for `t_student`
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `studentNo` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  `departmentObj` varchar(20) default NULL,
  `name` varchar(20) default NULL,
  `sex` varchar(4) default NULL,
  `birthday` varchar(20) default NULL,
  `telephone` varchar(20) default NULL,
  `studentPhoto` varchar(50) default NULL,
  `address` varchar(60) default NULL,
  `memo` longtext,
  PRIMARY KEY  (`studentNo`),
  KEY `FK1A9BE39F97E0739` (`departmentObj`),
  KEY `FKAEC90D50F97E0739` (`departmentObj`),
  CONSTRAINT `FK1A9BE39F97E0739` FOREIGN KEY (`departmentObj`) REFERENCES `t_department` (`departmentNo`),
  CONSTRAINT `FKAEC90D50F97E0739` FOREIGN KEY (`departmentObj`) REFERENCES `t_department` (`departmentNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('STU001', '123', 'BJ001', '李江', '男', '2018-03-10', '13598349834', 'upload/NoImage.jpg', '四川成都', '这个员工很勤奋');
INSERT INTO `t_student` VALUES ('STU002', '123', 'BJ001', '刘立伟', '男', '2018-03-12', '13598431343', 'upload/NoImage.jpg', '福建南平', '测试');

-- ----------------------------
-- Table structure for `t_studenttask`
-- ----------------------------
DROP TABLE IF EXISTS `t_studenttask`;
CREATE TABLE `t_studenttask` (
  `taskId` int(11) NOT NULL auto_increment,
  `title` varchar(60) default NULL,
  `content` longtext,
  `studentObj` varchar(20) default NULL,
  `taskFile` varchar(50) default NULL,
  `taskDate` varchar(20) default NULL,
  `chargeObj` varchar(20) default NULL,
  PRIMARY KEY  (`taskId`),
  KEY `FK7DE5A35EA2CBF239` (`studentObj`),
  KEY `FK7DE5A35E8B07C88D` (`chargeObj`),
  KEY `FKDE098EF519D9CAD5` (`studentObj`),
  KEY `FKDE098EF58B07C88D` (`chargeObj`),
  CONSTRAINT `FK7DE5A35E8B07C88D` FOREIGN KEY (`chargeObj`) REFERENCES `t_departcharge` (`chargeUserName`),
  CONSTRAINT `FK7DE5A35EA2CBF239` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`),
  CONSTRAINT `FKDE098EF519D9CAD5` FOREIGN KEY (`studentObj`) REFERENCES `t_student` (`studentNo`),
  CONSTRAINT `FKDE098EF58B07C88D` FOREIGN KEY (`chargeObj`) REFERENCES `t_departcharge` (`chargeUserName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_studenttask
-- ----------------------------
INSERT INTO `t_studenttask` VALUES ('1', '给你设计下数据库表', '要开发一个人事系统 你去设计下一个表吧', 'STU001', 'upload/2294b07d-3570-4bfa-8722-7ec4807fa97d.doc', '2018-03-06', 'teacher1');
INSERT INTO `t_studenttask` VALUES ('2', '开始员工管理模块开发', '部门管理模块已经测试完毕', 'STU001', 'upload/d7247a5b-0e00-48cd-9dc8-410f08b69f50.doc', '2018-03-12', 'teacher1');
INSERT INTO `t_studenttask` VALUES ('3', '开发工资管理模块', '人事工资系统工资模块的开发', 'STU002', 'upload/cea943d2-772c-4bcd-bbe1-3f7275f5a3a8.txt', '2018-03-12', 'teacher1');
