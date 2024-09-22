package avalon.client;


import java.util.Random;

public enum GoodCharacter implements Character {
    MERLIN, PERCIVAL, LOYAL_SERVANT_OF_ARTHUR;
    @Override
    public VoteTeamToken voteOnTeam() {
        return new Random().nextBoolean() ? VoteTeamToken.APPROVE : VoteTeamToken.REJECT;
    }

    @Override
    public QuestCard voteOnQuests() {
        return QuestCard.SUCCESS;
    }
}
