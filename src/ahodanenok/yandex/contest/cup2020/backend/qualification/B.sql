-- YandexCup2020.Backend.Qualification.B

WITH host_max_duration AS (
    SELECT job_name, host_name, max(strftime('%s', job_finished_time) - strftime('%s', trigger_fire_time)) as duration FROM qrtz_log WHERE job_status = 'OK' GROUP BY job_name, host_name
)
SELECT
  ql.job_name,
  ql.host_name,
  (CASE ql.job_status
    WHEN 'OK' THEN strftime('%s', ql.job_finished_time) - strftime('%s', ql.trigger_fire_time) ELSE 0
  END) as duration
FROM qrtz_log ql
JOIN (
  SELECT job_name, max(trigger_fire_time) as trigger_fire_time FROM qrtz_log WHERE job_status IS NOT NULL OR (job_status IS NULL
    AND strftime('%s', 'now') - strftime('%s', trigger_fire_time) > 2 * coalesce((SELECT d.duration FROM host_max_duration d WHERE d.job_name = job_name AND d.host_name = host_name), -1)) GROUP BY job_name
) last_job_info ON ql.job_name = last_job_info.job_name AND ql.trigger_fire_time = last_job_info.trigger_fire_time

--insert into qrtz_log VALUES (1, 'jobA',	'G1',	'2020-09-01 08:00:00', NULL,                    NULL,   'host1');
--insert into qrtz_log VALUES (2,	'jobA',	'G1',	'2020-09-01 07:00:00', '2020-09-01 07:00:03',	'OK',	'host1');
--insert into qrtz_log VALUES (3,	'jobA',	'G1',	'2020-09-01 06:00:00', '2020-09-01 06:00:04',	'OK',	'host2');
--insert into qrtz_log VALUES (4,	'jobB',	'G1',	'2020-09-01 08:00:00', NULL,	                'ERR',	'host1');
--insert into qrtz_log VALUES (5,	'jobB',	'G1',	'2020-09-01 07:00:00',	'2020-09-01 07:00:03',	'OK',	'host1');
--insert into qrtz_log VALUES (6,	'jobB',	'G1',	'2020-09-01 06:00:00',	'2020-09-01 06:00:04',	'OK',	'host1');
--insert into qrtz_log VALUES (7,	'jobC',	'G1',	'2020-09-01 08:00:00',	'2020-09-01 08:00:01',	'OK',	'host3');
--insert into qrtz_log VALUES (8,	'jobC',	'G1',	'2020-09-01 07:00:00',	NULL,                   NULL,	'host2');
--insert into qrtz_log VALUES (9,	'jobC',	'G1',	'2020-09-01 06:00:00',	'2020-09-01 06:00:04',	'OK',	'host1');
--insert into qrtz_log VALUES (10,	'jobD',	'G1',	'2020-09-01 09:59:59',	NULL,	                NULL,	'host1');
--insert into qrtz_log VALUES (11,	'jobD',	'G1',	'2020-06-01 07:00:00',	'2020-06-01 10:00:00',	'OK',	'host1');
--insert into qrtz_log VALUES (12,	'jobD',	'G1',	'2020-05-01 06:00:00',	'2020-05-01 10:00:00',	'OK',	'host1');
