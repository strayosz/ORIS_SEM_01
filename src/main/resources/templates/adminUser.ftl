<!DOCTYPE html>
<html lang="ru">
<div style="color:red">${errormessage!}</div>
<div style="color:greenyellow">${successmessage!}</div>
<form action="/slotSwap/admin/user" method="post">
    <div>
        <label>Имя</label>
        <label>${selectedUser.name}</label>
    </div>
    <div>
        <label>Фамилия</label>
        <label>${selectedUser.surname}</label>
    </div>
    <div>
        <label>Статус</label>
        <select name="selectedUserRole">
            <#if selectedUser.role == 'Стажер'>
                <option value="Стажер" selected>Стажер</option>
            <#else>
                <option value="Стажер">Стажер</option>
            </#if>
            <#if selectedUser.role == 'Младший куратор'>
                <option value="Младший куратор" selected>Младший куратор</option>
            <#else>
                <option value="Младший куратор">Младший куратор</option>
            </#if>
            <#if selectedUser.role == 'Старший куратор'>
                <option value="Старший куратор" selected>Старший куратор</option>
            <#else>
                <option value="Старший куратор">Старший куратор</option>
            </#if>
            <#if selectedUser.role == 'Админ'>
                <option value="Админ" selected>Админ</option>
            <#else>
                <option value="Админ">Админ</option>
            </#if>
        </select>
    </div>
    <input type="submit" value="Готово">
</form>
</html>