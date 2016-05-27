INSERT INTO activiti6ui.ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_)
VALUES ('admin', 1, 'admin', 'admin', 'avorona@betacom.com.pl', 'admin');


SELECT
  t.ASSIGNEE_    AS userId,
  t.PROC_DEF_ID_ AS procesKey,
  u.first_name,
  u.last_name,
  t.CREATE_TIME_
FROM activiti6ui.ACT_RU_TASK t
  LEFT JOIN activiti6ui.USERS u ON u.id = t.ASSIGNEE_;