let selectedCard = null;
function selectCard(card, value) {
    if (selectedCard) selectedCard.classList.remove('selected');
    selectedCard = card;
    card.classList.add('selected');
    document.getElementById('selectedRecord').value = value;
}