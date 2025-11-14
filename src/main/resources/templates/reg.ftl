<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Registration</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">
    <link rel="stylesheet" href="/slotSwap/static/css/input.css">
    <link rel="stylesheet" href="/slotSwap/static/css/error.css">
    <script src="/slotSwap/static/js/error.js"></script>
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
    </div>
</div>

<#if errormessage??>
    <div id = "error-block" class="error" onclick="hide()">
        <span class="text">${errormessage}</span>
    </div>
</#if>

<form action="/slotSwap/reg" method="post">
    <div class="form-wrapper">
        <h2>Введите данные для регистрации</h2>
        <div>
            <label>
                <input type="text" placeholder="Имя" name="name" required>
            </label>
        </div>

        <div>
            <label>
                <input type="text" placeholder="Фамилия" name="surname" required>
            </label>
        </div>
        <div>
            <label>
                <input type="text" placeholder="Логин" name="login" required minlength="8">
            </label>
        </div>
        <div>
            <label>
                <input type="password" placeholder="Пароль" name="password" required minlength="8">
            </label>
        </div>

        <div class ="buttons" style="justify-content: center">
            <div class="div-button">
                <input type="submit" value="Зарегестрироваться" class="button-login">
            </div>
        </div>
    </div>
</form>
</body>
</html>