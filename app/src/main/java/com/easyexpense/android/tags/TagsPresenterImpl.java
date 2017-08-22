package com.easyexpense.android.tags;

import com.geekmk.db.dto.CardTag;
import com.geekmk.db.dto.LedgerCard;

import java.util.Calendar;

/**
 * Created by Mani on 13/04/17.
 */

public class TagsPresenterImpl implements TagsPresenter {

    private TagsView view;


    public TagsPresenterImpl(TagsView view){
        this.view = view;
    }


    @Override
    public void addTag(long cardId, String tagName) {
        LedgerCard ledgerCard = LedgerCard.load(LedgerCard.class,cardId);
        CardTag cardTag = new CardTag();
        cardTag.setName(tagName);
        cardTag.setUpdatedTS(Calendar.getInstance().getTimeInMillis());
        cardTag.setCreatedTS(Calendar.getInstance().getTimeInMillis());
        cardTag.setCardId(ledgerCard.getId());
        cardTag.setDefault(false);
        cardTag.setBoardId(ledgerCard.getBoardId());


        view.addTagSuccess(cardTag.save());
    }
}
