<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
</head>
<body>
<h1>欢迎你，${user.name}！</h1>
<p>你的年龄是：${user.age}</p>
<p>你的爱好：
    <#list user.hobbies as hobby>
        ${hobby}
    </#list>
</p>
</body>
</html>