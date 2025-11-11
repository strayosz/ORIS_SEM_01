<!DOCTYPE html>
<html lang="ru">
<head>
    <title>User list</title>
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
<h1>
    Список пользователей
</h1>
<form action="/slotSwap/admin/user" method="get">
    <div>
        <tr>
            <th>Имя</th>
            <th>Фамилия</th>
            <th>Роль</th>
        </tr>
        <#list users as user>
            <div>
                <input type="radio" name="selectedUserLogin" value="${user.login}"/>
                <label>${user.name} ${user.surname} ${user.role}</label>
            </div>
        </#list>
        <p>Выберите пользователя для изменения данных</p>
        <input type="submit" value="Выбрать пользователя">
    </div>
</form>
</body>

</html>