package avalon.client;


import java.util.Random;

public enum EvilCharacter implements Character {
    MORDRED, MORGANA, ASSASSIN, OBERON, MINION_OF_MORDRED;

    @Override
    public VoteTeamToken voteOnTeam() {
        return new Random().nextBoolean() ? VoteTeamToken.APPROVE : VoteTeamToken.REJECT;
    }

    @Override
    public QuestCard voteOnQuests() {
        return new Random().nextBoolean() ? QuestCard.SUCCESS : QuestCard.FAILURE;
    }
}
