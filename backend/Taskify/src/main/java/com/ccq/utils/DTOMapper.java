package com.ccq.utils;

import java.util.stream.Collectors;

import com.ccq.pojo.Activity;
import com.ccq.pojo.Attachment;
import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.pojo.ChecklistItem;
import com.ccq.pojo.Comment;

import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.pojo.ListStatus;
import com.ccq.pojo.response.*;

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
        ResCardDTO dto = new ResCardDTO();
        dto.setId(card.getId());
        dto.setName(card.getName());
        dto.setDescription(card.getDescription());
        dto.setCreatedDate(card.getCreatedDate());
        dto.setIsActive(card.getIsActive());
        dto.setDueDate(card.getDueDate());
        dto.setReminderDate(card.getReminderDate());
        dto.setPosition(card.getPosition());

        if (card.getChecklistItemSet() != null) {
            dto.setChecklists(card.getChecklistItemSet().stream()
                    .map(DTOMapper::toChecklistItemDTO).collect(Collectors.toList()));
        }
        if (card.getActivitySet() != null) {
            dto.setActivities(card.getActivitySet().stream()
                    .map(DTOMapper::toActivityDTO).collect(Collectors.toList()));
        }
        if (card.getAttachmentSet() != null) {
            dto.setAttachments(card.getAttachmentSet().stream()
                    .map(DTOMapper::toAttachmentDTO).collect(Collectors.toList()));
        }
        if (card.getCommentSet() != null) {
            dto.setComments(card.getCommentSet().stream()
                    .map(DTOMapper::toCommentDTO).collect(Collectors.toList()));
        }
        if (card.getCardUserSet() != null) {
            dto.setAssignees(card.getCardUserSet().stream()
                    .map(cu -> toUserDTO(cu.getUserId())).collect(Collectors.toList()));
        }

        return dto;
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
}
