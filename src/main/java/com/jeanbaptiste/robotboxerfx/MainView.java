package com.jeanbaptiste.robotboxerfx;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainView {

    private Combat combat;

    private Label lblNomAtlas;
    private Label lblPvAtlas;
    private Label lblEnergieAtlas;

    private Label lblNomTitan;
    private Label lblPvTitan;
    private Label lblEnergieTitan;

    private Label lblTour;

    private ProgressBar pvAtlasBar;
    private ProgressBar pvTitanBar;

    private ProgressBar energieAtlasBar;
    private ProgressBar energieTitanBar;

    private Button btnJab;
    private Button btnDirect;
    private Button btnGarde;
    private Button btnEsquive;
    private Button btnSuperPunch;
    private Button btnRejouer;

    private TextArea zoneLog;

    private VBox root;
    private VBox blocAtlas;
    private VBox blocTitan;

    private ImageView imageAtlas;
    private ImageView imageTitan;

    public MainView() {
        Robot atlas = new Robot("Atlas", 100, 100, 18, 8);
        Robot titan = new Robot("Titan", 100, 100, 15, 10);
        combat = new Combat(atlas, titan);

        construireInterface();
        mettreAJourAffichage();
    }

    private void construireInterface() {
        lblTour = new Label("Tour : 1");
        lblTour.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        lblNomAtlas = new Label();
        lblPvAtlas = new Label();
        lblEnergieAtlas = new Label();

        lblNomTitan = new Label();
        lblPvTitan = new Label();
        lblEnergieTitan = new Label();

        var atlasUrl = getClass().getResource("/images/atlas.png");
        if (atlasUrl != null) {
            Image atlasImg = new Image(atlasUrl.toExternalForm());
            imageAtlas = new ImageView(atlasImg);
            imageAtlas.setFitWidth(120);
            imageAtlas.setPreserveRatio(true);
        } else {
            imageAtlas = new ImageView();
            System.out.println("atlas.png introuvable dans resources/images");
        }

        var titanUrl = getClass().getResource("/images/titan.png");
        if (titanUrl != null) {
            Image titanImg = new Image(titanUrl.toExternalForm());
            imageTitan = new ImageView(titanImg);
            imageTitan.setFitWidth(120);
            imageTitan.setPreserveRatio(true);
        } else {
            imageTitan = new ImageView();
            System.out.println("titan.png introuvable dans resources/images");
        }

        pvAtlasBar = new ProgressBar(1.0);
        pvAtlasBar.setPrefWidth(700);
        pvAtlasBar.setPrefHeight(75);
        pvAtlasBar.setStyle("-fx-accent: green; -fx-border-color: black; -fx-border-width: 1;");

        pvTitanBar = new ProgressBar(1.0);
        pvTitanBar.setPrefWidth(700);
        pvTitanBar.setPrefHeight(75);
        pvTitanBar.setStyle("-fx-accent: green; -fx-border-color: black; -fx-border-width: 1;");

        energieAtlasBar = new ProgressBar(1.0);
        energieAtlasBar.setPrefWidth(700);
        energieAtlasBar.setPrefHeight(75);
        energieAtlasBar.setStyle("-fx-accent: dodgerblue; -fx-border-color: black; -fx-border-width: 1;");

        energieTitanBar = new ProgressBar(1.0);
        energieTitanBar.setPrefWidth(700);
        energieTitanBar.setPrefHeight(75);
        energieTitanBar.setStyle("-fx-accent: dodgerblue; -fx-border-color: black; -fx-border-width: 1;");

        Label lblVieAtlas = new Label("Vie");
        Label lblEnergieTitreAtlas = new Label("Énergie");

        Label lblVieTitan = new Label("Vie");
        Label lblEnergieTitreTitan = new Label("Énergie");

        blocAtlas = new VBox(
                8,
                imageAtlas,
                lblNomAtlas,
                lblVieAtlas,
                pvAtlasBar,
                lblPvAtlas,
                lblEnergieTitreAtlas,
                energieAtlasBar,
                lblEnergieAtlas
        );
        blocAtlas.setPadding(new Insets(10));
        blocAtlas.setStyle("-fx-border-color: gray; -fx-border-width: 1;");

        blocTitan = new VBox(
                8,
                imageTitan,
                lblNomTitan,
                lblVieTitan,
                pvTitanBar,
                lblPvTitan,
                lblEnergieTitreTitan,
                energieTitanBar,
                lblEnergieTitan
        );
        blocTitan.setPadding(new Insets(10));
        blocTitan.setStyle("-fx-border-color: gray; -fx-border-width: 1;");

        HBox zoneRobots = new HBox(30, blocAtlas, blocTitan);

        btnJab = new Button("JAB");
        btnDirect = new Button("DIRECT");
        btnGarde = new Button("GARDE");
        btnEsquive = new Button("ESQUIVE");
        btnSuperPunch = new Button("SUPER PUNCH ⚡");
        btnRejouer = new Button("REJOUER");

        btnJab.setOnAction(e -> jouerTour(ActionRobot.JAB));
        btnDirect.setOnAction(e -> jouerTour(ActionRobot.DIRECT));
        btnGarde.setOnAction(e -> jouerTour(ActionRobot.GARDE));
        btnEsquive.setOnAction(e -> jouerTour(ActionRobot.ESQUIVE));
        btnSuperPunch.setOnAction(e -> jouerTour(ActionRobot.SUPER_PUNCH));

        btnRejouer.setDisable(true);
        btnRejouer.setOnAction(e -> recommencerCombat());

        HBox zoneBoutons = new HBox(10, btnJab, btnDirect, btnGarde, btnEsquive, btnSuperPunch, btnRejouer);

        zoneLog = new TextArea();
        zoneLog.setEditable(false);
        zoneLog.setWrapText(true);
        zoneLog.setPrefHeight(300);

        root = new VBox(15, lblTour, zoneRobots, zoneBoutons, zoneLog);
        root.setPadding(new Insets(20));
    }

    private void jouerTour(ActionRobot actionJoueur) {
        if (combat.combatTermine()) {
            return;
        }

        int pvTitanAvant = combat.getRobot2().getPointsDeVie();
        int pvAtlasAvant = combat.getRobot1().getPointsDeVie();

        ActionRobot actionTitan = combat.genererActionRobot2();
        String resultat = combat.jouerTour(actionJoueur, actionTitan);

        int pvTitanApres = combat.getRobot2().getPointsDeVie();
        int pvAtlasApres = combat.getRobot1().getPointsDeVie();

        zoneLog.appendText(resultat + "\n\n");
        mettreAJourAffichage();

        if (pvTitanApres < pvTitanAvant) {
            secouerRobot(blocTitan);
        }

        if (pvAtlasApres < pvAtlasAvant) {
            secouerRobot(blocAtlas);
        }

        if (combat.combatTermine()) {
            desactiverBoutons();
            btnRejouer.setDisable(false);
        }
    }

    private void mettreAJourAffichage() {
        Robot atlas = combat.getRobot1();
        Robot titan = combat.getRobot2();

        lblTour.setText("Tour : " + combat.getNumeroTour());

        lblNomAtlas.setText("Robot : " + atlas.getNom());
        lblPvAtlas.setText("PV : " + atlas.getPointsDeVie());
        lblEnergieAtlas.setText("Énergie : " + atlas.getEnergie());

        lblNomTitan.setText("Robot : " + titan.getNom());
        lblPvTitan.setText("PV : " + titan.getPointsDeVie());
        lblEnergieTitan.setText("Énergie : " + titan.getEnergie());

        pvAtlasBar.setProgress(atlas.getPointsDeVie() / 100.0);
        pvTitanBar.setProgress(titan.getPointsDeVie() / 100.0);

        energieAtlasBar.setProgress(atlas.getEnergie() / 100.0);
        energieTitanBar.setProgress(titan.getEnergie() / 100.0);

        if (atlas.getPointsDeVie() > 60) {
            pvAtlasBar.setStyle("-fx-accent: green;");
        } else if (atlas.getPointsDeVie() > 30) {
            pvAtlasBar.setStyle("-fx-accent: orange;");
        } else {
            pvAtlasBar.setStyle("-fx-accent: red;");
        }

        if (titan.getPointsDeVie() > 60) {
            pvTitanBar.setStyle("-fx-accent: green;");
        } else if (titan.getPointsDeVie() > 30) {
            pvTitanBar.setStyle("-fx-accent: orange;");
        } else {
            pvTitanBar.setStyle("-fx-accent: red;");
        }
    }

    private void desactiverBoutons() {
        btnJab.setDisable(true);
        btnDirect.setDisable(true);
        btnGarde.setDisable(true);
        btnEsquive.setDisable(true);
        btnSuperPunch.setDisable(true);
    }

    private void recommencerCombat() {
        Robot atlas = new Robot("Atlas", 100, 100, 18, 8);
        Robot titan = new Robot("Titan", 100, 100, 15, 10);
        combat = new Combat(atlas, titan);

        btnJab.setDisable(false);
        btnDirect.setDisable(false);
        btnGarde.setDisable(false);
        btnEsquive.setDisable(false);
        btnSuperPunch.setDisable(false);
        btnRejouer.setDisable(true);

        zoneLog.clear();
        zoneLog.appendText("=== Nouveau combat ===\n\n");

        mettreAJourAffichage();
    }

    private void secouerRobot(VBox bloc) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), bloc);
        shake.setByX(20);
        shake.setCycleCount(8);
        shake.setAutoReverse(true);
        shake.play();
    }

    public Parent getRoot() {
        return root;
    }
}