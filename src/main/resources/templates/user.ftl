<!DOCTYPE html>
<html lang="ru">

<h1>
    Ваш профиль, ${user.name} ${user.surname}!
</h1>
<div>
    ${user.login}
</div>
<div>
    ${user.role}
</div>
<form action="/slotSwap/user/update" method="get">
    <input type="submit" value="Изменить данные">
</form>
<form action="/slotSwap/logout" method="get">
    <input type="submit" value="Выйти">
</form>

</html>