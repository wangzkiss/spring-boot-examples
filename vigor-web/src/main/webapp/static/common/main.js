$(function(){
	// 列表头部固定
	var tableFixed = {
        box: '.table-fixed',
        table: '.table-fixed__table',
        cHeadFixed: '.table-fixed__fixed-head',

        init : function () {
            if ($(this.box).length > 0) {
                this.cloneHead();
                this.scroll();
                // this.resize();
            }
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
            $('.ichecks-all').on('ifChecked', function(){
                $('.i-checks').iCheck('check');
            }).on('ifUnchecked', function(){
                $('.i-checks').iCheck('uncheck');
            });
        },

        cloneHead : function () {
            var that = this,
                box = this.box,
                table = this.table;

            $(box).each(function(index, el) {
                var colgroup = $(el).find('colgroup').clone(),
                    thead = $(el).find('thead').clone();

                $(el).find(table).clone().prependTo(el).removeClass('table-fixed__table').addClass('table-fixed__fixed-head').find('tbody').remove();
                that.maxHeight(el);
                $(window).resize(function() {
                    that.maxHeight(el);
                });

            });
            
        },

        scroll : function () {
            var that = this;

            $(that.box).scroll(function(event) {
                $(this).find(that.cHeadFixed).css('top', $(this).scrollTop());
            });
        },

        maxHeight : function (el, marginBottom) {
            var top = $(el).offset().top,
                winHeight = $(window).height(),
                // bottom = marginBottom || 60,
                bottom = marginBottom || 150,
                height = winHeight - top - bottom;
                if (height < 300) {
                    height = 300;
                }
            $(el).css({'max-height' : height + 'px'});
        },

        resize : function(el) {
            var that = this;

        }
    }
    window.tableHeadFixed = tableFixed.init();
})