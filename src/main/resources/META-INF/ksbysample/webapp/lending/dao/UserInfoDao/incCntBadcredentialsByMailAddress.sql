update
  user_info
set
  cnt_badcredentials = cnt_badcredentials + 1
where
  mail_address = /* mailAddress */'tanaka.taro@sample.com'
  and cnt_badcredentials < 5
