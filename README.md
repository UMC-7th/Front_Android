# 이거먹자-Android
> UMC 7th 1st Place Team <br>
24.12.30 - 25.02.21

![이거먹자](https://github.com/user-attachments/assets/802aa3fb-be25-4623-8d95-b45d8781e48e)
![image](https://github.com/user-attachments/assets/9d56917b-472a-4e8b-8dec-2a82962368e5)


<div align="center">

## 🍨 *****Contributors*****

| 손주완(Lead) <br> [@vvan2](https://github.com/vvan2) | 김민주 <br> [@MINJU](https://github.com/MINJU-62) | 성규현 <br> [@dmp100](https://github.com/dmp100) | 최희원 <br> [@Choiheewon](https://github.com/heewon411) |
|:---:|:---:|:---:|:---:|
| <img width="200" src="https://github.com/user-attachments/assets/40d71434-fafc-437a-a8c9-2dcc276bc329"/> | <img width="200" src="https://github.com/user-attachments/assets/2204e157-2142-41a1-9357-5166daabb644"/> | <img width="200" src="https://github.com/user-attachments/assets/48571048-0733-41db-923f-83b97fa858ea"/> | <img width="200" src="https://github.com/user-attachments/assets/8631248c-6d9b-49f4-95f2-f956d3c22393"/> |
|`스플래시`   `로그인`  `회원가입`<br> `애니메이션`  `알림설정` <br>   `내 정보관리` `장바구니` <br> `결제` `시세/상세페이지` | `식단/오늘`  `식단 등록내역` <br> `수동등록` `식재료 시세`<br> `식단 레시피`  `맛있는 일상 구독` <br> `즐겨찾기`  | `식단/월간`  `시세/메인` <br> `구독관리`  `구독내역` <br> `구독식단조회`    | `온보딩` `설문조사` <br> `주소록 관리`|

</div>


<br/>

<div align="center">

## 🟨 *****SCREENSHOT*****
| 온보딩,로그인 | 설문조사 | 식단 |
|:---:|:---:|:---:|
| <img width="200" src="https://github.com/user-attachments/assets/c43c2fec-6e2a-459f-9b71-3637db952e83"/> | <img width="200" src="https://github.com/user-attachments/assets/e3731a75-dee7-435b-8f86-b7325acaa921"/> | <img width="200" src="https://github.com/user-attachments/assets/ac7c9ee3-0a1b-415f-be58-14e33da43123"/> |
| 시세 | 구독 | 마이 |
| <img width="200" src="https://github.com/user-attachments/assets/bd749f82-3427-4b4e-83f1-18e468ed5508"/> | <img width="200" src="https://github.com/user-attachments/assets/d94119f8-afb6-4ecd-b30e-d5286c632cd1"/> | <img width="200" src="https://github.com/user-attachments/assets/fd0df426-9ee2-4cc6-b04d-57dd5ab6c5e7"/> |

</div>

<br/>





## 📗 *****Convention*****
[📱 FE 공간](https://www.notion.so/FE-1867ed2cd0e181598911e1e71e3aa139?pvs=4)
<br>
[🚀 Git Convention + Branch Strategy](https://www.notion.so/Git-Convention-Branch-Strategy-1867ed2cd0e18188a6eff164b18c7965?pvs=4)
<br>
[✏️Figma](https://www.figma.com/design/XswgLf9BzXFH49Ae4N578k/Wireframe_Kim-Taehyun(Tommy)?node-id=279-1632&p=f&t=YE8OZFATJhky8Zfc-0)
<br/>

## 🔧 *****TECH STACKS*****
| **Category** | **TechStack** |
| --- | --- |
| Language | Kotlin |
| Network | Retrofit, OkHttp, Gson |
| Asynchronous | Coroutines |
| Jetpack | ViewBinding, Navigation, DataStore |
| Image | Glide |

<br/>

## 📁 *****Foldering*****

```
📂 com.example.umc
┣ 📂 core
┃ ┣ 📂 designsystem
┃ ┃ ┣ 📂 theme
┃ ┃ ┣ 📂 component
┃ ┃ ┗ 📂 util
┃ ┣ 📂 navigation
┃ ┗ 📂 state
┣ 📂 data
┃ ┣ 📂 datasource
┃ ┣ 📂 datasourceimpl  
┃ ┣ 📂 model
┃ ┃ ┣ 📂 request
┃ ┃ ┗ 📂 response
┃ ┣ 📂 repositoryimpl
┃ ┗ 📂 service
┣ 📂 domain
┃ ┣ 📂 model
┃ ┣ 📂 repository
┃ ┗ 📂 usecase
┃   ┣ 📂 user
┃   ┗ 📂 diet
┗ 📂 presentation
 ┣ 📂 diet
 ┃ ┗ 📂 adapter
 ┣ 📂 mypage
 ┣ 📂 survey  
 ┗ 📂 subscribe
