貸出申請が承認・却下されました。
========================================================================
承認／却下　書籍
------------------------------------------------------------------------
<#list mail003BookDataList as bookData>
　${bookData.approvalResultStr}　　　${bookData.bookName}
</#list>
========================================================================

詳細は以下のURLから確認してください。

http://localhost:8080/confirmresult?lendingAppId=${lendingAppId}
