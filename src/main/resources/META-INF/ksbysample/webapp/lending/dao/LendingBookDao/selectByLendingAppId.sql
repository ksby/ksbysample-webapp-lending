select
  /*%expand*/*
from
  lending_book
where
  lending_app_id = /* lendingAppId */1
order by
  lending_book_id
