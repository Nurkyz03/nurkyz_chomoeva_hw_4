import java.util.Random;

public class Main {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {290, 270, 250, 250, 500, 250, 260, 280};
    public static int[] heroesDamage = {25, 15, 10, 0, 5, 0, 0, 20};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};
    public static int roundNumber = 0;
    public static boolean thorStunned = false;


    public static void main(String[] args) {
        showStatistics();
        while (!isGameOver()) {
            round();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void round() {
        roundNumber++;
        chooseBossDefence();
        if (!thorStunned) {
            bossAttacks();
            heroesAttack();
        } else {
            System.out.println("Boss is stunned and skips this round!");
            thorStunned = false;
        }
        medicHeal();
        luckyEvade();
        witcherRevive();
        showStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2,3,4,5,6
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && !thorStunned) {
                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = damage * coeff;
                    System.out.println("Critical damage: " + heroesAttackType[i] + " " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
                if (heroesAttackType[i].equals("Thor")) { //  // Проверяем, оглушен ли босс Тором
                    Random random = new Random();
                    if (random.nextBoolean()){
                        thorStunned = true;
                        System.out.println("Thor stuns the boss!");
                    }
                }
            }
        }
    }

    public static void bossAttacks() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                int damageToHero = bossDamage;
                if (!heroesAttackType[i].equals("Golem")) { // Если герой не Golem, уменьшаем урон
                    damageToHero -= bossDamage / 5;
                }
                if (heroesAttackType[i].equals("Golem")) { // Если герой - Golem
                    damageToHero += (heroesAttackType.length-1) * (bossDamage/5); // принимает на себя урон босса + 1/5 часть урона исходящего от босса по другим игрокам.
                }
                if (heroesHealth[i] - damageToHero < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] -= damageToHero;
                }
            }
        }
    }

    public static void showStatistics() {
        System.out.println("--------- " + "ROUND " + roundNumber + " ---------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage
                + " defence: " + (bossDefence == null ? "None" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] +
                    " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }

    public static void medicHeal() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && heroesHealth[i] < 100 && i != 3) {
                Random random = new Random();
                int healAmount = random.nextInt(30) + 10;
                heroesHealth[i] += healAmount;
                System.out.println("Medic heals " + heroesAttackType[i] + " for " + healAmount + " health.");
                break; // Medic может лечить только одного героя за раунд
            }
        }
    }

    public static void luckyEvade() {
        Random random = new Random();
        boolean evaded = random.nextBoolean(); // Проверяем уклонился ли Lucky от удара
        if (evaded) {
            System.out.println("Lucky evades the boss's attack!");
        }
    }

    public static void witcherRevive() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] <= 0 && heroesHealth[6]>0 ) {
                // Находим первого погибшего героя и оживляем его
                heroesHealth[i] = heroesHealth[6];
                System.out.println("Witcher revived " + heroesAttackType[i] + " and dies.");
                // После оживления погибает сам Witcher
                heroesHealth[6] = 0;
                break; // Выходим из цикла после успешного оживления
            }
        }
    }
}