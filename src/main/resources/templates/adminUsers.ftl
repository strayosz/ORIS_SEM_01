<!DOCTYPE html>
<html lang="ru">
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
</html>