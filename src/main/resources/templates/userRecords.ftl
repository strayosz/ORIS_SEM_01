<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Records</title>
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
<form method="post" action="/slotSwap/user/records">
    <div>
        <label>Выбрать тип смены</label>
        <select name="recordsType">
            <option value="sheduled" selected>Запланированные</option>
            <option value="exchanged" selected>Обмен</option>
            <option value="completed" selected>Завершенные</option>
        </select>
    </div>
    <div>
        <input type="submit" value="Показать">
    </div>
</form>
<#if records??>
    <#if recordsType != 'completed'>
        <h1>
            ${tableName}
        </h1>
        <div>
            <table>
                <thead>
                <tr>
                    <th>Тип</th>
                    <th>Дата</th>
                    <th>Время</th>
                    <th>Название</th>
                    <th>Статус</th>
                </tr>
                </thead>
                <tbody>
                <#list records as record>
                    <tr>
                        <td data-label="Тип">${record.slot.type}</td>
                        <td data-label="Дата">${record.slot.date}</td>
                        <td data-label="Время">${record.slot.time}</td>
                        <td data-label="Название">${record.slot.name!"Отсутствует"}</td>
                        <td data-label="Статус">${record.status}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <#if recordsType == 'sheduled'>
            <form method="post" action="/slotSwap/user/records">
                <div>
                    <label>Выбрать смену для передачи</label>
                    <select name="choosedRecordId">
                        <#list records as record>
                            <option value="${record.id};${recordsType}">${record.slot.type}, ${record.slot.date}, ${record.slot.time}, ${record.status}</option>
                        </#list>
                    </select>
                </div>
                <div>
                    <input type="submit" value="Отдать">
                </div>
            </form>
        <#elseif recordsType == 'exchanged'>
            <form method="post" action="/slotSwap/user/records">
                <div>
                    <label>Выбрать смену для отозвания</label>
                    <select name="choosedRecordId">
                        <#list records as record>
                            <option value="${record.id};${recordsType}">${record.slot.type}, ${record.slot.date}, ${record.slot.time}, ${record.status}</option>
                        </#list>
                    </select>
                </div>
                <div>
                    <input type="submit" value="Отозвать">
                </div>
            </form>
        </#if>
    <#else>
        <h1>
            ${tableName}
        </h1>
        <div>
            <table>
                <thead>
                <tr>
                    <th>Тип</th>
                    <th>Дата</th>
                    <th>Время</th>
                    <th>Название</th>
                    <th>Количество чатов</th>
                    <th>Комментарий</th>
                    <th>Статус</th>
                </tr>
                </thead>
                <tbody>
                <#list records as record>
                    <tr>
                        <td data-label="Тип">${record.slot.type}</td>
                        <td data-label="Дата">${record.slot.date}</td>
                        <td data-label="Время">${record.slot.time}</td>
                        <td data-label="Название">${record.slot.name!"Отсутствует"}</td>
                        <td data-label="Количество чатов">${record.chatsCount!"Не заполнено"}</td>
                        <td data-label="Комментарий">${record.comment!"Отсутствует"}</td>
                        <td data-label="Статус">${record.status}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </#if>
</#if>
<#if recordsIsEmpty??>
    <h1>
       ${recordsIsEmpty!} <#if recordsType == 'sheduled'> для обмена <#elseif recordsType == 'exchanged'> для отзыва <#else> завершенных
        </#if>
    </h1>
</#if>

<form action="/slotSwap/home" method="get">
    <input type="submit" value="Домой">
</form>
</body>
</html>