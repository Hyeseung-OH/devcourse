"use client";

import { fetchApi } from "@/lib/client";
import { PostDto, PostCommentDto } from "@/type/post";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Home() {
  const { id : postId } = useParams();
  const router = useRouter();

  const [post, setPost] = useState<PostDto | null>(null);
  const [postComments, setPostComments] = useState<PostCommentDto[] | null>(null);


  useEffect(() => {
    fetchApi(`/api/v1/posts/${postId}`)
    .then(setPost)
    .catch((err) => {
      alert(err);
      router.replace("/posts");
    });    fetchApi(`/api/v1/posts/${postId}/comments`).then(setPostComments);
  }, []);


  const deletePost = (postId: number) => {
    fetchApi(`/api/v1/posts/${postId}`, {
      method: "DELETE",
    }).then((data) => {
      alert(data.msg);
      router.replace("/posts");
    });
  };

  const deletePostComment = (commentId: number) => {
    fetchApi(`/api/v1/posts/${postId}/comments/${commentId}`, {
      method: "DELETE",
    }).then((data) => {
      alert(data.msg);
      
      if (postComments === null) return;

      // 리렌더링을 위한 댓글 배열 교체 필요
      setPostComments(
        postComments.filter((postComment) => postComment.id !== commentId)
      );
    })
  }

  if (post === null) {
    return <div>Loading...</div>;
  }

  return (
    <>
        <h1 className="p-2">글 상세 보기</h1>

        <div>
          <div>번호 : {post.id}</div>
          <div>제목 : {post.title}</div>
          <div>내용 : {post.content}</div>
        </div>

        <div className="flex gap-4">
          <Link className="border-2 p-2 rounded" href={`/posts/${post.id}/edit`}>
            수정
          </Link>
          <button
            className="border-2 p-2 rounded"
            onClick={() => {
              deletePost(post.id);
            }}
          >
            삭제
          </button>
        </div>

        <h2 className="p-2">댓글 목록</h2>
        {postComments === null && <div>Loading...</div>}
        {postComments !== null && postComments.length === 0 && (
          <div>댓글이 없습니다.</div>
        )}

        {postComments !== null && postComments.length > 0 && (
          <ul>
            {postComments.map((postComment) => (
              <li key={postComment.id} className="flex gap-2">
                <span>{postComment.id}: </span>
                <span>{postComment.content}</span>
                <button className="border-2 p-2 rounded">수정</button>
                <button className="border-2 p-2 rounded"
                onClick={() => {deletePostComment(postComment.id)}}>삭제</button>
              </li>
            ))}
          </ul>
      )}
    </>
  );
}