<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Login</title>
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
    </div>
</div>

<div style="color:red">${errormessage!}</div>

<form action="/slotSwap/usercheck" method="post">
    <div class="form-wrapper">
        <h2>Введите логин и пароль</h2>
        <div>
            <label>
                <input type="text" placeholder="Логин" name="login">
            </label>
        </div>

        <div>
            <label>
                <input type="password" placeholder="Пароль" name="password">
            </label>
        </div>

        <div class = "buttons">
            <div class="div-button">
                <input type="submit" value="Вход" class="button-login">
            </div>

            <div class="div-button">
                <a href="/slotSwap/reg" class="button-reg">Регистрация</a>
            </div>
        </div>

    </div>
</form>

</body>
</html>