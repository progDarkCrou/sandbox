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

SELECT *
FROM activiti6ui.ACT_ID_USER;

SELECT
  e.ACT_ID_ 'Process starter ID',
  e.NAME_   'Process inst. name',
  u.ID_     'User id',
  u.FIRST_  'First name'
FROM ACT_RU_EXECUTION e LEFT JOIN ACT_ID_USER u ON e.ACT_ID_ = u.ID_;

SELECT *
FROM ACT_RU_EXECUTION e;

SELECT *
FROM ACT_RU_EXECUTION e
  JOIN ACT_ID_USER u ON e.REV_ = u.REV_;

SELECT *
FROM ACT_RU_EXECUTION e LEFT JOIN ACT_ID_USER u ON e.REV_ = u.REV_;

SELECT *
FROM ACT_RU_EXECUTION e INNER JOIN ACT_ID_USER u ON e.REV_ = u.REV_;

SELECT *
FROM ACT_RU_EXECUTION e RIGHT JOIN ACT_ID_USER u ON e.REV_ = u.REV_;

SELECT *
FROM ACT_RU_EXECUTION e
  JOIN ACT_ID_USER u ON e.REV_ = u.REV_;

SELECT *
FROM ACT_RU_IDENTITYLINK;

SELECT
  u.id 'User id',
  count(*)
FROM USERS u
  JOIN ACT_RU_IDENTITYLINK idl ON idl.USER_ID_ = u.id
GROUP BY u.id;
