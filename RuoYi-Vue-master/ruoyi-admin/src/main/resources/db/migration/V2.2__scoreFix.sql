alter table `t_bayonet_battle_achievement` add column `happen_time` int comment '发生秒数';

alter table `t_bayonet_battle_achievement` add column `attacker_id` bigint comment '攻击者';

alter table `t_bayonet_battle_achievement` add column `opponent_id` bigint comment '被击中者';

alter table `t_bayonet_battle_achievement` drop foreign key t_bayonet_battle_achievement_ibfk_2;
alter table `t_bayonet_battle_achievement` drop column `person_id`;


