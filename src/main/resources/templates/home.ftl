<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Home</title>
    <link rel="stylesheet" href="/slotSwap/static/css/up-panel.css">
    <link rel="stylesheet" href="/slotSwap/static/css/card.css">
    <link rel="stylesheet" href="/slotSwap/static/css/input.css">
    <link rel="stylesheet" href="/slotSwap/static/css/profile.css">
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
<#if isEmptyExchanged??>
    <div class = "empty" style="margin-top: 30px">
        <span>${isEmptyExchanged}</span>
    </div>
<#else>
    <h1>
        Доступные для обмена смены
    </h1>
    <div>
        <div class="card-container">
            <#list records as record>
                <div class="record-card" onclick="selectCard(this, '${record.id}')">
                    <div class="record-title">${record.slot.name!"Без названия"}</div>
                    <div class="record-info">
                        <p><b>Тип</b> ${record.slot.type}</p>
                        <p><b>Дата</b> ${record.slot.date}</p>
                        <p><b>Время</b> ${record.slot.time}</p>
                        <p><b>Владелец</b> ${record.user.name} ${record.user.surname}</p>
                    </div>
                </div>
            </#list>
        </div>
        <div class = "form-wrapper">
            <label>
                <input type="text" placeholder="Комментарий" name="comment">
            </label>
        </div>
        <div style = "display: flex; justify-content: center">
            <form method="post" action="/slotSwap/home">
                <input type="hidden" id="selectedRecord" name="choosedRecordId">
                <input type="submit" value="Взять" class = "button-login">
            </form>
        </div>
    </div>
</#if>
</body>
</html>