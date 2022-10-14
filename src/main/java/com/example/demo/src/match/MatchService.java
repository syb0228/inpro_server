package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.PostUserLikeRes;
import com.example.demo.src.match.model.PostUserPassRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MatchService {

    private final MatchProvider matchProvider;
    private final MatchDao matchDao;

    public void updateUserLike(int userLikeIdx) throws BaseException {
        try {
            int result = matchDao.updateUserLike(userLikeIdx);
            if(result == 0){
                throw new BaseException(FAIL_USERLIKE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserLikeRes createUserLike(int likerIdx, int likingIdx) throws BaseException {
        // likingIdx 유효성 검사
        if(matchProvider.checkUserIdx(likingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 중복 좋아요 유효성 검사
        if(matchProvider.checkPreUserLike(likerIdx, likingIdx) == 1){
            throw new BaseException(USERLIKE_INVALID_LIKINGIDX);
        }

        try {
            // 이전에 비활성화나 좋아요 취소를 했던 유저의 경우
            int preUserLikeIdx = matchProvider.checkUserLikeHist(likerIdx, likingIdx);
            if(preUserLikeIdx != 0){
                // active 업데이트
                updateUserLike(preUserLikeIdx);
                return new PostUserLikeRes(preUserLikeIdx);
            }

            int userLikeIdx = matchDao.createUserLike(likerIdx, likingIdx);
            return new PostUserLikeRes(userLikeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUserLike(int likerIdx, int likingIdx) throws BaseException {
        // likingIdx 유효성 검사
        if(matchProvider.checkUserIdx(likingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 좋아요 누르지 않은 유저에 대해 좋아요 취소 요청을 할 경우
        if(matchProvider.checkPreUserLike(likerIdx, likingIdx) == 0){
            throw new BaseException(UNUSERLIKE_INVALID_LIKINGIDX);
        }

        try {
            int result = matchDao.deleteUserLike(likerIdx, likingIdx);
            if(result == 0){
                throw new BaseException(FAIL_UNUSERLIKE);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateUserPass(int userPassIdx) throws BaseException {
        try {
            int result = matchDao.updateUserPass(userPassIdx);
            if(result == 0){
                throw new BaseException(FAIL_USERPASS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserPassRes createUserPass(int passerIdx, int passingIdx) throws BaseException {
        // passingIdx 유효성 검사
        if(matchProvider.checkUserIdx(passingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 중복 넘기기 유효성 검사
        if(matchProvider.checkPreUserPass(passerIdx, passingIdx) == 1){
            throw new BaseException(USERPASS_INVALID_PASSINGIDX);
        }

        try {
            // 이전에 비활성화나 넘기기 취소를 했던 유저의 경우
            int preUserPassIdx = matchProvider.checkUserPassHist(passerIdx, passingIdx);
            if(preUserPassIdx != 0){
                // active 업데이트
                updateUserPass(preUserPassIdx);
                return new PostUserPassRes(preUserPassIdx);
            }

            int userPassIdx = matchDao.createUserPass(passerIdx, passingIdx);
            return new PostUserPassRes(userPassIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}