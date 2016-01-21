select
  /*%expand*/*
from
  lending_book
where
  lending_app_id = /* lendingAppId */105
  /*%if lendingAppFlg != null */
    and lending_app_flg = /* lendingAppFlg */'1'
  /*%end*/
order by
  lending_book_id
