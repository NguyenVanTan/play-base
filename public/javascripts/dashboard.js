function collectReceiverIds() {
    var receiverIds = "";
    $('.cboUser').each(function() {
        if(this.checked){
            receiverIds += this.value + ",";
        }
    });
    $('#receiverIds').val(receiverIds.length > 0 ? receiverIds.slice(0, -1) : "");
}

$('#cboAll').click(function() {
    var checked = this.checked;
    $('.cboUser').each(function() {
        this.checked = checked;
    });
    collectReceiverIds();
});

$('.cboUser').click(function(){
    $('#cboAll').prop('checked',$('.cboUser:checked').length == $('.cboUser').length);
    collectReceiverIds();
});

$('#btnSave').click(function () {
    $('#noticeStatus').val("2");
});

$('#btnSaveAsDraft').click(function () {
    $('#noticeStatus').val("3");
});

$('#btnCreateNewNotice').click(function () {
    activeForm(true);
    $('#noticeMessage').val("");
    $('#noticeMessage').focus();
    $(':checkbox').each(function(){
        this.checked = false;
    });
    $('#noticeTitle').html("Create a notice")
    $('#saveType').val("1");
});

$('.draftNotice').click(function(){
    activeForm(true);
    $("#noticeForm").trigger('reset');
    $('#noticeMessage').val($(this).children('#msg-content').html());
    $('#noticeId').val($(this).children('#notice-id-content').html());

    var s = $(this).children('#receiver-content').html();
    choiceReceiver(s);
    $('#receiverIds').val(s);
    $('#saveType').val("2");
    $('#noticeTitle').html("Update notice or sending ...")
});

$('.sentNotice').click(function(){
    activeForm(false);
    $("#noticeForm").trigger('reset');
    $('#noticeMessage').val($(this).children('#msg-content').html());
    $('#noticeId').val($(this).children('#notice-id-content').html());
    choiceReceiver($(this).children('#receiver-content').html());
    $('#noticeTitle').html("Click 'Create New' to creating new notice ...")
});

function choiceReceiver (receiver) {
    var listReceiver = receiver.split(",");
    listReceiver.forEach(function (t) {
        $(':checkbox').each(function(){
            if(this.value == t){
                this.checked = true;
            }
        });
    });
    $('#cboAll').prop('checked', $('.cboUser').length == listReceiver.length);
}

function activeForm(enabled) {
    $("#noticeMessage").attr("readonly", !enabled);
    $(':checkbox').each(function(){
        $(this).attr("disabled", !enabled);
    });
    $('#btnSave').attr("disabled", !enabled);
    $('#btnSaveAsDraft').attr("disabled", !enabled);
}

$('.removeNotice').click(function () {
    if (confirm("Are you sure to remove it ?") == true) {
        $('#noticeId').val($(this).attr("val"));
        $('#saveType').val("3");
        $('#noticeForm').submit();
    } else {
        $('#noticeId').val("");
        $('#saveType').val("2");
    }
});
