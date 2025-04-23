drop table if exists `t_bayonet_battle`;
CREATE TABLE `t_bayonet_battle`  (
  `id` bigint,
  `config_id` bigint,
  `start_time` datetime,
  `end_time` datetime,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  foreign key(config_id) references t_bayonet_competition_config(id)
) ENGINE = InnoDB comment '刺杀演习记录表';

drop table if exists `t_bayonet_battle_achievement`;
CREATE TABLE `t_bayonet_battle_achievement`  (
    `id` bigint,
    `battle_id` bigint,
    `person_id` bigint,
    `hit_area_id` bigint,
    `hit_times` int,
    `hit_strength` int,
    `create_time` datetime,
    `update_time` datetime,
    PRIMARY KEY (`id`),
    foreign key(battle_id) references t_bayonet_battle(id),
    foreign key(person_id) references t_bayonet_personnel(id),
    foreign key(hit_area_id) references t_bayonet_hit_area_config(id)
) ENGINE = InnoDB comment '刺杀演习成绩记录表';


