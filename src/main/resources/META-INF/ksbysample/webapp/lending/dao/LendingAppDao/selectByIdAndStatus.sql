select
  /*%expand*/*
from
  lending_app
where
  lending_app_id = /* lendingAppId */105
  /*%if statusList != null */
    and status in /* statusList */('3', '4')
  /*%end*/
