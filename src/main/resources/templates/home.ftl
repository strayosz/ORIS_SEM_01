<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Home</title>
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
    Добро пожаловать!
</h1>
<form action="/slotSwap/user" method="get">
    <input type="submit" value="Профиль">
</form>
<#if isEmptyExchanged??>
    <div style="color:red">${isEmptyExchanged}</div>
<#else>
    <h1>
        Доступные для обмена смены
    </h1>
    <div>
        <table>
            <thead>
            <tr>
                <th>Тип</th>
                <th>Дата</th>
                <th>Время</th>
                <th>Название</th>
                <th>Имя владельца</th>
                <th>Фамилия владельца</th>
            </tr>
            </thead>
            <tbody>
            <#list records as record>
                <tr>
                    <td data-label="Тип">${record.slot.type}</td>
                    <td data-label="Дата">${record.slot.date}</td>
                    <td data-label="Время">${record.slot.time}</td>
                    <td data-label="Название">${record.slot.name!"Отсутствует"}</td>
                    <td data-label="Имя владельца">${record.user.name}</td>
                    <td data-label="Фамилия владельца">${record.user.surname}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
    <form method="post" action="/slotSwap/home">
        <div>
            <label>Выбрать смену для передачи</label>
            <select name="choosedRecordId">
                <#list records as record>
                    <option value="${record.id}">${record.slot.type}, ${record.slot.date}, ${record.slot.time}, ${record.slot.name!}, ${record.user.name}, ${record.user.surname} </option>
                </#list>
            </select>
        </div>
        <div>
            <label>Введите комментарий</label>
            <input type="text" name="comment">
        </div>
        <div>
            <input type="submit" value="Взять">
        </div>
    </form>
</#if>
</body>
</html>