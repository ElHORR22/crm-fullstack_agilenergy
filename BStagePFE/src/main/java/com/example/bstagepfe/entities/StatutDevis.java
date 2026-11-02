package com.example.bstagepfe.entities;

import java.util.Set;

/**
 * Cycle de vie d'un devis :
 * BROUILLON -> ENVOYE -> ACCEPTE ou REFUSE.
 * Un devis envoyé peut repasser en brouillon pour être corrigé ;
 * ACCEPTE et REFUSE sont définitifs.
 */
public enum StatutDevis {
    BROUILLON,
    ENVOYE,
    ACCEPTE,
    REFUSE;

    public Set<StatutDevis> transitionsPossibles() {
        return switch (this) {
            case BROUILLON -> Set.of(ENVOYE);
            case ENVOYE -> Set.of(ACCEPTE, REFUSE, BROUILLON);
            case ACCEPTE, REFUSE -> Set.of();
        };
    }

    public boolean peutPasserA(StatutDevis cible) {
        return cible != null && transitionsPossibles().contains(cible);
    }

    /** Seul un brouillon peut être modifié (lignes, destinataire, conditions). */
    public boolean estModifiable() {
        return this == BROUILLON;
    }

    /** Un devis accepté fait partie du chiffre d'affaires : on ne le supprime pas. */
    public boolean estSupprimable() {
        return this == BROUILLON || this == REFUSE;
    }

    /** Le client ne voit pas les brouillons. */
    public boolean estVisibleParClient() {
        return this != BROUILLON;
    }
}
