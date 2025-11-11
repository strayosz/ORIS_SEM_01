<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Edit</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">

</head>
<body>
<div class="up-panel">
    <div class="div-up-panel">
        <a href="/slotSwap/home" class="home-button"></a>
    </div>
    <div class="div-up-panel">
        <span class="up-panel-name">SLOT SWAP</span>
    </div>
    <div class="div-up-panel">
        <a href="/slotSwap/user" class="profile-button"></a>
    </div>
</div>

<div style="color:red">${errormessage!}</div>
<div style="color:greenyellow">${successmessage!}</div>
<form action="/slotSwap/user/update" method="post">
    <div>
        <label>Имя</label>
        <input type="text" name="name" value = "${user.name}">
    </div>
    <div>
        <label>Фамилия</label>
        <input type="text" name="surname" value = "${user.surname}">
    </div>
    <div>
        <label>Старый Пароль</label>
        <input type="password" name="oldPassword">
    </div>
    <div>
        <label>Новый Пароль</label>
        <input type="password" name="newPassword">
    </div>
    <input type="submit" value="Готово">
</form>
<form action="/slotSwap/home" method="get">
    <input type="submit" value="Домой">
</form>
</body>
</html>