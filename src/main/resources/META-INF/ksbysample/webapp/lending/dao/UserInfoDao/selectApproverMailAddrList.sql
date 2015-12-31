select
  ui.mail_address
from
  user_role ur
  , user_info ui
where
  ur.role = 'ROLE_APPROVER'
  and ui.user_id = ur.user_id
