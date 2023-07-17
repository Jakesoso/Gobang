
// 画出棋盘
function draw_board() {
    const gameBoard = document.querySelector('#game-board');
    for (let i = 0; i < 15; i++) {
        for (let j = 0; j < 15; j++) {
            const cell = document.createElement('div');
            cell.classList.add('game-cell');
            cell.dataset.row = i;
            cell.dataset.col = j;
            gameBoard.appendChild(cell);
        }
    }
}

function dropping(cell, board) {
    const currentPlayer = document.querySelector('#current-player');
    let isBlackTurn = board.isBlackTurn();
    let step;

    // 添加已落子的样式类
    cell.classList.add(isBlackTurn ? 'black' : 'white', 'occupied');
    // 显示棋子
    cell.style.backgroundPosition = "center";
    cell.style.backgroundRepeat = "no-repeat"
    cell.style.backgroundImage = isBlackTurn ?"url('./image/black.png')":"url('./image/white.png')"
    cell.style.backgroundSize = '80%';

    // 更新Java类数据
    board.update(cell.dataset.row, cell.dataset.col);

    isBlackTurn = board.isBlackTurn();
    step = board.getSteps();

    // 显示落子信息
    currentPlayer.textContent = `当前落子方：${isBlackTurn ? '黑方' : '白方'}`;
    currentPlayer.innerHTML += '&nbsp;&nbsp;&nbsp;&nbsp;共 ' + step + ' 步';

    // 判断是否胜利
    board.setWin();
    if(board.getWin()){
        currentPlayer.textContent = `${!isBlackTurn ? '黑方' : '白方'}胜利！`;
    }
}

function multi_player() {
    const cells = document.querySelectorAll('.game-cell');
    // 为每个格子添加点击事件
    cells.forEach(cell => {
        cell.addEventListener('click', () => {
            if (!cell.classList.contains('occupied') && !board.getWin()) {
                dropping(cell, board); // 落子
            }
        });
    });
}

function single_player() {
    // 获取先手
    const blackPlayer = document.getElementById("black-btn");
    const whitePlayer = document.getElementById("white-btn");

    blackPlayer.addEventListener("click", function() {
        gobangAI.setBlack(2);
        single_start();
        document.getElementById("player-select").style.display = "none";
    });

    whitePlayer.addEventListener("click", function() {
        gobangAI.setBlack(1);
        single_start();
        document.getElementById("player-select").style.display = "none";
    });
}

function ai_dropping() {
    // ai判断后返回一个最优点
    point = gobangAI.aiJudge();
    // 构造cell
    let x = point.getX();
    let y = point.getY();
    let score = point.getScore();
    alert("AI: x = " + x + ", y = " + y + ", score = " + score)
    let ai_cell = document.querySelectorAll('.game-cell[data-row="' + x + '"][data-col="' + y + '"]')[0];
    dropping(ai_cell, gobangAI.getBoard());
}

function single_start() {
    const currentPlayer = document.querySelector('#current-player');
    const cells = document.querySelectorAll('.game-cell');
    currentPlayer.textContent = `当前落子方：黑方`;

    if (gobangAI.getBlack() == 1) {
        // 先手AI随机下
        let x = Math.floor(Math.random() * 6) + 5;
        let y = Math.floor(Math.random() * 6) + 5;
        let ai_cell = document.querySelectorAll('.game-cell[data-row="' + x + '"][data-col="' + y + '"]')[0];
        dropping(ai_cell, gobangAI.getBoard());
        //ai_dropping();
        gobangAI.getBoard().setAiTurn(false);

    }

    // 为每个格子添加点击事件
    cells.forEach(cell => {
        cell.addEventListener('click', () => {
            if (!cell.classList.contains('occupied') && !gobangAI.getBoard().getWin() && !gobangAI.getBoard().isAiTurn()) {
                dropping(cell, gobangAI.getBoard());
                // 如果玩家胜利 则直接结束
                if (gobangAI.getBoard().getWin()) {
                    return;
                }
                gobangAI.getBoard().setAiTurn(true);
                // AI下棋
                ai_dropping();
                gobangAI.getBoard().setAiTurn(false);
            }
        });
    });

}