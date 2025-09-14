"use client";

import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function Home() {
  const { id } = useParams();

  const [post, setPost] = useState<{
    id: number;
    title: string;
    content: string;
  } | null>(null);

  useEffect(() => {
    fetch(`http://localhost:8070/api/v1/posts/${id}`)
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setPost(data);
      });
  }, []);

  return (
    <>
      <div className="flex flex-col gap-8">
        <h1>글 상세 보기</h1>

        {post === null && <div>Loading...</div>}

        {post !== null && (
          <div>
            <div>번호 : {post.id}</div>
            <div>제목 : {post.title}</div>
            <div>내용 : {post.content}</div>
          </div>
        )}
      </div>
    </>
  );
}