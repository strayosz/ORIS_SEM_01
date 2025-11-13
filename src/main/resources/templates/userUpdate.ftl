<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Edit</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">
    <link rel="stylesheet" href="/slotSwap/static/css/input.css">
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
    <div class="form-wrapper">
        <div>
            <label>
                <input type="text" name="name" value="${user.name}" placeholder="Имя">
            </label>
        </div>
        <div>
            <label>
                <input type="text" name="surname" value="${user.surname}" placeholder="Фамилия">
            </label>
        </div>
        <div>
            <label>
                <input type="password" name="oldPassword" placeholder="Старый Пароль">
            </label>
        </div>
        <div>
            <label>
                <input type="password" name="newPassword" placeholder="Новый Пароль">
            </label>
        </div>
        <div class="buttons" style="justify-content: center">
            <div class="div-button">
                <input type="submit" value="Готово" class="button-login">
            </div>
        </div>
    </div>
</form>
</body>
</html>