<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Records</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">
    <link rel="stylesheet" href="/slotSwap/static/css/card.css">
    <link rel="stylesheet" href="/slotSwap/static/css/input.css">
    <script src="/slotSwap/static/js/card.js"></script>
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
<form method="get" action="/slotSwap/user/records" class = "select-record-type">
    <div>
        <label style="margin-bottom: 3px">Выберите тип смены</label>
        <label>
            <select name="recordsType" style = "width: 300px; height: 45px; margin-bottom: 10px; font-weight: 200">
                <option value="sheduled" <#if recordsType?? && recordsType == "sheduled">selected</#if>>Запланированные</option>
                <option value="exchanged" <#if recordsType?? && recordsType == "exchanged">selected</#if>>Обмен</option>
                <option value="completed" <#if recordsType?? && recordsType == "completed">selected</#if>>Завершенные
                </option>
            </select>
        </label>
        <input type="submit" value="Показать" class="button-reg"
               style="padding: 8px 16px; border-radius: 27px; background-color: white; font-size: 16px">
    </div>
</form>
<#if records??>
    <div>
        <div class="card-container">
            <#list records as record>
                <div class="record-card" onclick="selectCard(this, '${record.id};${recordsType}')">
                    <div class="record-title">${record.slot.name!"Без названия"}</div>
                    <div class="record-info">
                        <p><b>Тип</b> ${record.slot.type}</p>
                        <p><b>Дата</b> ${record.slot.date}</p>
                        <p><b>Время</b> ${record.slot.time}</p>
                        <p><b>Статус</b> ${record.status}</p>
                        <#if recordsType == 'completed'>
                        <p><b>Количество чатов</b> ${record.chatsCount!"Не заполнено"}</p>
                        <p><b>Комментарий</b> ${record.comment!"Отсутствует"}</p>
                        </#if>
                    </div>
                </div>
            </#list>
        </div>
        <div style = "display: flex; justify-content: center">
        <form method="post" action="/slotSwap/user/records">
            <input type="hidden" id="selectedRecord" name="choosedRecordId">
            <#if recordsType == 'sheduled' || recordsType == 'exchanged'>
                <input type="submit" value="<#if recordsType == 'sheduled'>Отдать<#else>Отозвать</#if>" class = "button-login" onclick="return isSelect()">
            </#if>
        </form>
        </div>
    </div>
<#else>
    <#if recordsType??>
        <div class="empty">
            <span>Этот список пуст</span>
        </div>
    </#if>
</#if>
</body>
</html>