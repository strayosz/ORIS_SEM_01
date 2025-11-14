<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Transactions</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">
    <link rel="stylesheet" href="/slotSwap/static/css/profile.css">
    <link rel="stylesheet" href="/slotSwap/static/css/table.css">
    <link rel="stylesheet" href="/slotSwap/static/css/card.css">
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
<#if transactions??>
    <h1 style="margin-bottom: 3px">
        История обменов
    </h1>
    <div>
        <table>
            <thead>
            <tr>
                <th>Кто отдал</th>
                <th>Кто взял</th>
                <th>Что обменяли</th>
                <th>Дата и время обмена</th>
                <th>Комментарий обмена</th>
            </tr>
            </thead>
            <tbody>
            <#list transactions as t>
                <tr>
                    <td data-label="Кто отдал">${t.fromUser.name} ${t.fromUser.surname}</td>
                    <td data-label="Кто взял">${t.toUser.name} ${t.toUser.surname}</td>
                    <td data-label="Что обменяли">${t.slot.type} ${t.slot.date} ${t.slot.time}</td>
                    <td data-label="Дата и время обмена">${t.date} ${t.time}</td>
                    <td data-label="Комментарий обмена">${t.comment}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
<#else>
    <div class="empty">
        <span>Этот список пуст</span>
    </div>
</#if>
</body>
</html>