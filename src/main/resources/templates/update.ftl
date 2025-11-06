<!DOCTYPE html>
<html lang="ru">
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
        <label>Пароль</label>
        <input type="password" name="password">
    </div>
    <input type="submit" value="Готово">
</form>
<form action="/slotSwap/home" method="get">
    <input type="submit" value="Домой">
</form>
</html>