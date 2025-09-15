"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { PostDto } from "@/type/post";

export default function Home() {
  const [posts, setPosts] = useState<PostDto[] | null>(null);
  const baseurl = process.env.NEXT_PUBLIC_API_BASE_URL;
  
  useEffect(() => {
    fetch(`${baseurl}/api/v1/posts`)
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setPosts(data);
      });
  }, []);

  return (
    <>
      <div className="flex flex-col gap-9">
        <h1>글 목록</h1>
        {posts === null && <div>Loading...</div>}
        {posts !== null && posts.length === 0 && <div>글이 없습니다.</div>}
        {posts !== null && posts.length > 0 && (
          <ul>
            {posts.map((post) => (
              <li key={post.id}>
                <Link href={`/posts/${post.id}`}>
                  {post.id} : {post.title}
                </Link>
              </li>
            ))}
          </ul>
        )}
      </div>
      <div>
        <Link href="/posts/write">새 글 작성</Link>
      </div>
    </>
  );
}