// 手机号
$.validator.addMethod("mobile_var", function(value, element) {
	return this.optional(element) || /^1[3|4|5|7|8]\d{9}$/i.test(value);
}, "手机号码格式不正确");
// 电话号码
$.validator.addMethod("phone_var", function(value, element) {
	return this.optional(element) || /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/i.test(value);
}, "电话号码格式不正确");
// 验证ip地址，允许输入${ip_name}
$.validator.addMethod("ip_var", function(value, element) {
	return this.optional(element) || /(^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^\$\{\w+\}$)/i.test(value);
}, "请输入有效的IP地址");
// 验证ip地址
$.validator.addMethod("ip_name", function(value, element) {
	return this.optional(element) || /(^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$)/i.test(value);
}, "请输入有效的IP地址");
// 验证多个ip地址
$.validator.addMethod("ip_names", function(value, element) {
	return this.optional(element) ||  /^((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?),?)*$/i.test(value);
}, "请输入有效的IP地址");
//用户名、密码、函数名之类
$.validator.addMethod("name_pass", function(value, element) {
	return this.optional(element) || /(^[\w+!@#$%^&*()+=]+$|^\$\{\w+\}$)/i.test(value);
}, "请输入数字、字母、下划线");
//密码专属
$.validator.addMethod("exclusivePass", function(value, element) {
	return this.optional(element) || /(^[\w+!@#$%^~?&·;',`][/\<.>*()+-=]+$|^\$\{\w+\}$)/i.test(value);
}, "请输入数字、字母、下划线");
//表名、目录等,
$.validator.addMethod("table_name", function(value, element) {
	return this.optional(element) || /(^[\w\/]+$|^\$\{\w+\}$)/i.test(value);
}, "请输入数字，字母，下划线");
//函数类名
$.validator.addMethod("func_class_name", function(value, element) {
	return this.optional(element) || /^(\w)+[\(\s\.\)\?\w]+$/i.test(value);
}, "请输入数字、字母、小数点或下划线");
//字母
$.validator.addMethod("english", function(value, element) {
	return this.optional(element) || /^[A-Za-z]+$/i.test(value);
}, "请输入字母");
//请输入数字、字母或下划线
$.validator.addMethod("w_w", function(value, element) {
	return this.optional(element) || /^\w+$/i.test(value);
}, "请输入数字、字母或下划线");
//非中文
$.validator.addMethod("notChinese", function(value, element) {
	return this.optional(element) || !/[\u4e00-\u9fa5]/i.test(value);
}, "不支持输入中文");
// 非 —— 
$.validator.addMethod("notBar", function(value, element) {
	return this.optional(element) || !/—/i.test(value);
}, "无效字符&nbsp;—");