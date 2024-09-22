package avalon.client;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
    Character character;
    boolean isMerlin;
    boolean isAssassin;
    boolean isLeader;
    int round;
    ArrayList<String> playersInLobby = new ArrayList<>();
    ArrayList<String> evilPlayers = new ArrayList<>();
    public boolean isMerlin() {
        return isMerlin;
    }

    public void setAssassin(boolean assassin) {
        isAssassin = assassin;
    }

    public void setMerlin(boolean merlin) {
        isMerlin = merlin;
    }

    public boolean isAssassin() {
        return isAssassin;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    public String voteForQuestTeam(){
        return character.voteOnTeam() == VoteTeamToken.APPROVE ? "approve" : "reject";
    }
    public String selectTeamMatesForQuest() {
        Collections.shuffle(playersInLobby);
        Pair<Integer, Integer> numOfPlayersInRound = new Pair<>(playersInLobby.size(), round);
        int numberOfPlayersToSelect = TeamAssignment.instance.assignments.get(numOfPlayersInRound);
        String teamMatesSelected = "/team";
        for(String player: playersInLobby.subList(0, numberOfPlayersToSelect)) {
            teamMatesSelected += (" " + player);
        }
        return teamMatesSelected;
    }
}
