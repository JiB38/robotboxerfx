package com.jeanbaptiste.robotboxerfx;

import java.util.Random;

public class Combat {

    private Robot robot1;
    private Robot robot2;
    private Random random;
    private int numeroTour;

    public Combat(Robot robot1, Robot robot2) {
        this.robot1 = robot1;
        this.robot2 = robot2;
        this.random = new Random();
        this.numeroTour = 1;
    }

    public String jouerTour(ActionRobot actionRobot1, ActionRobot actionRobot2) {
        StringBuilder message = new StringBuilder();

        message.append("=== Tour ").append(numeroTour).append(" ===\n");
        message.append(executerAction(robot1, robot2, actionRobot1)).append("\n");

        if (!robot2.estKO()) {
            message.append(executerAction(robot2, robot1, actionRobot2)).append("\n");
        }

        if (combatTermine()) {
            Robot gagnant = getGagnant();
            if (gagnant != null) {
                message.append("\n🏆 Le gagnant est ").append(gagnant.getNom()).append(" !");
            } else {
                message.append("\nMatch nul !");
            }
        }

        numeroTour++;
        return message.toString();
    }

    private String executerAction(Robot attaquant, Robot defenseur, ActionRobot action) {
        switch (action) {
            case JAB:
                return executerJab(attaquant, defenseur);
            case DIRECT:
                return executerDirect(attaquant, defenseur);
            case GARDE:
                return executerGarde(attaquant);
            case ESQUIVE:
                return executerEsquive(attaquant);
            case SUPER_PUNCH:
                return executerSuperPunch(attaquant, defenseur);
            default:
                return "Action inconnue.";
        }
    }

    private String executerJab(Robot attaquant, Robot defenseur) {
        int coutEnergie = 10;

        if (!attaquant.aAssezEnergie(coutEnergie)) {
            return attaquant.getNom() + " n'a pas assez d'énergie pour lancer un JAB.";
        }

        attaquant.consommerEnergie(coutEnergie);
        int degats = calculerDegats(attaquant, defenseur, 8);
        defenseur.subirDegats(degats);

        String message;
            if (degats == 0) {
                message = attaquant.getNom() + " lance un JAB, mais " + defenseur.getNom() + " esquive l'attaque !";
            } else {
                message = attaquant.getNom() + " lance un JAB sur " + defenseur.getNom()
                    + " et inflige " + degats + " dégâts.";

                    if (degats > 30) {
                        message += " 💥 CRITICAL HIT !";
                    }
                }

        defenseur.reinitialiserEffetsTemporaires();
        return message;
    }

    private String executerDirect(Robot attaquant, Robot defenseur) {
        int coutEnergie = 20;

        if (!attaquant.aAssezEnergie(coutEnergie)) {
            return attaquant.getNom() + " n'a pas assez d'énergie pour lancer un DIRECT.";
        }

        attaquant.consommerEnergie(coutEnergie);
        int degats = calculerDegats(attaquant, defenseur, 15);
        defenseur.subirDegats(degats);

        String message;
        if (degats == 0) {
            message = attaquant.getNom() + " tente un DIRECT, mais " + defenseur.getNom() + " esquive parfaitement !";
        } else {
            message = attaquant.getNom() + " envoie un DIRECT sur " + defenseur.getNom()
                    + " et inflige " + degats + " dégâts.";
                if (degats > 40) {
                    message += " 💥 CRITICAL HIT !";
                    }
                }

        defenseur.reinitialiserEffetsTemporaires();
        return message;
    }

    private String executerGarde(Robot robot) {
        robot.activerGarde();
        robot.recupererEnergie(5);
        return robot.getNom() + " se met en GARDE et récupère 5 points d'énergie.";
    }

    private String executerEsquive(Robot robot) {
        robot.activerEsquive();
        robot.recupererEnergie(5);
        return robot.getNom() + " prépare une ESQUIVE et récupère 5 points d'énergie.";
    }

    private int calculerDegats(Robot attaquant, Robot defenseur, int bonusAttaque) {

    if (defenseur.isModeEsquive()) {
        int chance = random.nextInt(100);
        if (chance < 40) {
            return 0;
        }
    }

    int degats = attaquant.getAttaque() + bonusAttaque - defenseur.getDefense();

    if (defenseur.isModeGarde()) {
        degats = degats / 2;
    }

    if (degats < 1) {
        degats = 1;
    }

    // Coup critique (20% de chance)
    int critique = random.nextInt(100);
    if (critique < 20) {
        degats = degats * 2;
    }

    return degats;
    }

    private String executerSuperPunch(Robot attaquant, Robot defenseur) {
    int coutEnergie = 35;

    if (!attaquant.aAssezEnergie(coutEnergie)) {
        return attaquant.getNom() + " n'a pas assez d'énergie pour lancer un SUPER PUNCH ⚡.";
    }

    attaquant.consommerEnergie(coutEnergie);

    int degats = calculerDegats(attaquant, defenseur, 28);
    defenseur.subirDegats(degats);

    String message;
    if (degats == 0) {
        message = attaquant.getNom() + " tente un SUPER PUNCH ⚡, mais "
                + defenseur.getNom() + " esquive l'attaque !";
    } else {
        message = attaquant.getNom() + " déclenche un SUPER PUNCH ⚡ sur "
                + defenseur.getNom() + " et inflige " + degats + " dégâts !";
    }

    if (degats > 45) {
        message += " 💥 CRITICAL HIT !";
    }

    defenseur.reinitialiserEffetsTemporaires();
    return message;
    }
    

    public ActionRobot genererActionRobot2() {
        ActionRobot[] actions = ActionRobot.values();
        return actions[random.nextInt(actions.length)];
    }

    public boolean combatTermine() {
        return robot1.estKO() || robot2.estKO();
    }

    public Robot getGagnant() {
        if (robot1.estKO() && robot2.estKO()) {
            return null;
        }
        if (robot1.estKO()) {
            return robot2;
        }
        if (robot2.estKO()) {
            return robot1;
        }
        return null;
    }

    public int getNumeroTour() {
        return numeroTour;
    }

    public Robot getRobot1() {
        return robot1;
    }

    public Robot getRobot2() {
        return robot2;
    }
}
