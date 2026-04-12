package com.ccq.utils;

import java.util.stream.Collectors;

import com.ccq.pojo.Activity;
import com.ccq.pojo.Attachment;
import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.pojo.ChecklistItem;
import com.ccq.pojo.Comment;
import com.ccq.pojo.ListStatus;
import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.pojo.response.ResActivityDTO;
import com.ccq.pojo.response.ResAttachmentDTO;
import com.ccq.pojo.response.ResBoardDTO;
import com.ccq.pojo.response.ResCardDTO;
import com.ccq.pojo.response.ResChecklistItemDTO;
import com.ccq.pojo.response.ResCommentDTO;
import com.ccq.pojo.response.ResListDTO;
import com.ccq.pojo.response.ResUserDTO;
import com.ccq.pojo.response.ResUserWorkspaceDTO;
import com.ccq.pojo.response.ResWorkspaceDTO;

public class DTOMapper {

    public static ResUserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new ResUserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getAvatar(), user.getRole(), user.getCreatedDate());
    }

    public static ResWorkspaceDTO toWorkspaceDTO(Workspace workspace) {
        if (workspace == null) {
            return null;
        }
        ResWorkspaceDTO dto = new ResWorkspaceDTO();
        dto.setId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setOwner(toUserDTO(workspace.getOwnerId()));
        return dto;
    }

    public static ResActivityDTO toActivityDTO(Activity activity) {
        if (activity == null) {
            return null;
        }
        return new ResActivityDTO(
                activity.getId(),
                activity.getActivity(),
                activity.getCreatedDate(),
                toUserDTO(activity.getUserId())
        );
    }

    public static ResAttachmentDTO toAttachmentDTO(Attachment attachment) {
        if (attachment == null) {
            return null;
        }
        return new ResAttachmentDTO(
                attachment.getId(),
                attachment.getFilename(),
                attachment.getUrl(),
                attachment.getUpdateDate()
        );
    }

    public static ResChecklistItemDTO toChecklistItemDTO(ChecklistItem item) {
        if (item == null) {
            return null;
        }
        return new ResChecklistItemDTO(
                item.getId(),
                item.getName(),
                item.getIsChecked(),
                item.getPosition()
        );
    }

    public static ResCommentDTO toCommentDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new ResCommentDTO(
                comment.getId(),
                comment.getComment(),
                comment.getCreatedDate(),
                toUserDTO(comment.getUserId())
        );
    }

    public static ResCardDTO toCardDTO(Card card) {
        if (card == null) {
            return null;
        }
        int listId = 0;
        if (card.getListId() != null && card.getListId().getId() != null) {
            listId = card.getListId().getId();
        }

        Integer position = card.getPosition();
        int safePosition = position != null ? position : 0;

        return new ResCardDTO(
                card.getId(),
                card.getName(),
                card.getDescription(),
                Boolean.TRUE.equals(card.getIsActive()),
                card.getDueDate(),
                card.getReminderDate(),
                safePosition,
                listId
        );
    }

    public static ResListDTO toListDTO(Boardlist boardlist) {
        if (boardlist == null) {
            return null;
        }
        ResListDTO dto = new ResListDTO();
        dto.setId(boardlist.getId());
        dto.setName(boardlist.getName());
        dto.setPosition(boardlist.getPosition());
        dto.setStatus(boardlist.getStatus() != null ? ListStatus.valueOf(boardlist.getStatus()) : null);

        if (boardlist.getCardSet() != null) {
            dto.setCards(boardlist.getCardSet().stream()
                    .map(DTOMapper::toCardDTO).collect(Collectors.toList()));
        }
        return dto;
    }

    public static ResBoardDTO toBoardDTO(Board board) {
        if (board == null) {
            return null;
        }
        ResBoardDTO dto = new ResBoardDTO();
        dto.setId(board.getId());
        dto.setName(board.getName());
        dto.setCreatedDate(board.getCreatedDate());
        dto.setIsPublic(board.getIsPublic());

        if (board.getBoardlistSet() != null) {
            dto.setLists(board.getBoardlistSet().stream()
                    .map(DTOMapper::toListDTO).collect(Collectors.toList()));
        }
        return dto;
    }

    public static ResUserWorkspaceDTO toUserWorkspaceDTO(UserWorkspace uw) {
        if (uw == null) {
            return null;
        }
        ResUserWorkspaceDTO dto = new ResUserWorkspaceDTO(uw.getUserId().getId(), uw.getWorkspaceId().getId());
        return dto;
    }
}
