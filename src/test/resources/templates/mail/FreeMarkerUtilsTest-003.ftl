氏名             年齢  住所
----------------------------------------------------------
<#list userList as user>
${user.name!?right_pad(8, "　")}  ${user.age!?left_pad(3)}  ${user.address!}
</#list>
