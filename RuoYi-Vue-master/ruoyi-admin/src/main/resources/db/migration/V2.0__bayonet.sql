drop table if exists `t_bayonet_unit`;
CREATE TABLE `t_bayonet_unit`  (
  `id` bigint,
  `unit_name` varchar(128),
  `leader` varchar(64),
  `quantity` int,
  `remark` varchar(128),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB comment '刺杀单位信息表';
INSERT INTO t_bayonet_unit (id,unit_name,leader,quantity) VALUES
    (1,'第一支队','张三',10),
    (2,'第二支队','李四',10),
    (3,'第三支队','王五',25),
    (4,'第四支队','赵六',25);

drop table if exists `t_bayonet_personnel`;
CREATE TABLE `t_bayonet_personnel`  (
  `id` bigint,
  `unit_id` bigint,
  `name` varchar(128),
  `num` int,
  `gender` int,
  `age` int,
  `remark` varchar(128),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`unit_id`, `num`),
  foreign key(unit_id) references t_bayonet_unit(id)
) ENGINE = InnoDB comment '刺杀人员信息表';

drop table if exists `t_bayonet_hit_area_config`;
CREATE TABLE `t_bayonet_hit_area_config`  (
  `id` bigint,
  `hit_code` int,
  `hit_area_name` varchar(32),
  `remark` varchar(128),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`hit_code`),
  UNIQUE KEY(`hit_area_name`)
) ENGINE = InnoDB comment '击中部位配置信息表';
INSERT INTO t_bayonet_hit_area_config (id,hit_code,hit_area_name,remark,create_time,update_time) VALUES
	 (1443144401114234880,1,'头部',NULL,'2021-09-29 17:23:32',NULL),
	 (1443144926371119104,2,'左胸',NULL,'2021-09-29 17:25:37',NULL),
	 (1443144962152726528,3,'右胸',NULL,'2021-09-29 17:25:46',NULL),
	 (1443145005865762816,4,'左臂',NULL,'2021-09-29 17:25:56',NULL),
	 (1443145035217502208,5,'右臂',NULL,'2021-09-29 17:26:03',NULL),
	 (1443145074652348416,6,'左腹',NULL,'2021-09-29 17:26:13',NULL),
	 (1443145105769889792,7,'右腹',NULL,'2021-09-29 17:26:20',NULL);

drop table if exists `t_bayonet_score_plan`;
CREATE TABLE `t_bayonet_score_plan`  (
  `id` bigint,
  `plan_name` varchar(32),
  `remark` varchar(128),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`plan_name`)
) ENGINE = InnoDB comment '刺杀分值方案配置信息表';
INSERT INTO t_bayonet_score_plan (id,plan_name,remark,create_time,update_time) VALUES
	 (1443380641344389120,'基础方案','基础方案','2021-09-30 09:02:16',NULL);

drop table if exists `t_bayonet_hit_score`;
CREATE TABLE `t_bayonet_hit_score`  (
  `id` bigint,
  `hit_area_id` bigint,
  `plan_id` bigint,
  `score` int,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`hit_area_id`, `plan_id`),
  foreign key(hit_area_id) references t_bayonet_hit_area_config(id),
  foreign key(plan_id) references t_bayonet_score_plan(id)
) ENGINE = InnoDB comment '击中部位分值配置信息表';
INSERT INTO t_bayonet_hit_score (id,hit_area_id,plan_id,score,create_time,update_time) VALUES
	 (1443380641549910016,1443144401114234880,1443380641344389120,15,'2021-09-30 09:02:16',NULL),
	 (1443380641579270144,1443144926371119104,1443380641344389120,10,'2021-09-30 09:02:16',NULL),
	 (1443380641596047360,1443144962152726528,1443380641344389120,10,'2021-09-30 09:02:16',NULL),
	 (1443380641625407488,1443145005865762816,1443380641344389120,5,'2021-09-30 09:02:16',NULL),
	 (1443380641675739136,1443145035217502208,1443380641344389120,5,'2021-09-30 09:02:16',NULL),
	 (1443380641747042304,1443145074652348416,1443380641344389120,8,'2021-09-30 09:02:16',NULL),
	 (1443380641759625216,1443145105769889792,1443380641344389120,8,'2021-09-30 09:02:16',NULL);

drop table if exists `t_bayonet_competition_config`;
CREATE TABLE `t_bayonet_competition_config`  (
  `id` bigint,
  `serial_no` int,
  `competition_name` varchar(64),
  `score_plan_id` bigint,
  `limited_time` int,
  `distance` varchar(64),
  `remark` varchar(128),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY(`serial_no`),
  UNIQUE KEY(`competition_name`),
  foreign key(score_plan_id) references t_bayonet_score_plan(id)
) ENGINE = InnoDB comment '刺杀方案配置信息表';

drop table if exists `t_bayonet_competition_group`;
CREATE TABLE `t_bayonet_competition_group`  (
  `id` bigint,
  `config_id` bigint,
  `memberId1` bigint,
  `memberId2` bigint,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  foreign key(config_id) references t_bayonet_competition_config(id),
  foreign key(memberId1) references t_bayonet_personnel(id),
  foreign key(memberId2) references t_bayonet_personnel(id)
) ENGINE = InnoDB comment '刺杀方案对抗组配置信息表';