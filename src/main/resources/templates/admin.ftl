<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Admin</title>
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
    <form action="/slotSwap/admin/users" method="get">
        <input type="submit" value="Показать всех пользователей">
    </form>
    <form action="/slotSwap/admin/transactions" method="get">
        <input type="submit" value="Показать все обмены">
    </form>
</body>

</html>