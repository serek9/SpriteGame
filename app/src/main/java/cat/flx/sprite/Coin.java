package cat.flx.sprite;

class Coin extends Character {

    private static int[][] states = {
            { 29, 30, 31, 32, 33 }
    };
    int[][] getStates() { return states; }

    Coin(Game game) {
        super(game);
        padLeft = padTop = 0;
        colWidth = colHeight = 12;
        frame = (int)(Math.random() * 5);
    }

    void physics() {
    }
}
