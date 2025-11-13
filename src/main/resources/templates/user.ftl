<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">
    <link rel="stylesheet" href="/slotSwap/static/css/profile.css">
</head>

<body>
<div class="up-panel">
    <div class="div-up-panel">
        <a href="/slotSwap/home" class="home-button"></a>
    </div>
    <div class="div-up-panel">
        <span class="up-panel-name">SLOT SWAP</span>
    </div>
    <div class="div-up-panel"></div>
</div>
<h1>
    Ваш профиль, ${user.name} ${user.surname}!
</h1>
<div class="container">
    <div class=block style="height: 120px">
        <div class = "buttons-div" style = "border-bottom-left-radius: 0; border-bottom-right-radius: 0; pointer-events: none;">
            <a> <b>Логин: </b><span style="margin-left: 8px"> ${user.login}</a>
        </div>
        <div class = "buttons-div" style = "border-top-left-radius: 0; border-top-right-radius: 0; pointer-events: none;">
            <a> <b>Роль: </b><span style="margin-left: 8px"> ${user.role}</a>
        </div>
    </div>

    <#if user.role == 'Админ'>
        <div class="block" style="height: 120px">
            <div class="buttons-div" style="border-bottom-left-radius: 0; border-bottom-right-radius: 0;">
                <a href="/slotSwap/admin/users">Показать всех пользователей</a>
            </div>
            <div class="buttons-div" style="border-top-left-radius: 0; border-top-right-radius: 0;">
                <a href="/slotSwap/admin/transactions">Показать историю обменов</a>
            </div>
        </div>

    </#if>

    <div class="block">
        <div class="buttons-div" style = "border-bottom-left-radius: 0; border-bottom-right-radius: 0">
            <a href="/slotSwap/user/update" class="buttons">Изменить данные</a>
        </div>

        <div class="buttons-div" style = "border-radius: 0;">
            <a href="/slotSwap/user/records">Посмотреть мои смены</a>
        </div>
        <div class="buttons-div" style="border-top-left-radius: 0; border-top-right-radius: 0">
            <a href="/slotSwap/login">Выйти из аккаунта</a>
        </div>
    </div>
</div>
</body>
</html>