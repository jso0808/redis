const datasets = [
  {
    title: "기상청 단기예보 조회서비스",
    desc: "동네예보/초단기실황. Redis 캐시 실습에 가장 적합.",
    link: "https://www.data.go.kr/data/15084084/openapi.do",
    useCase: "도시별 현재 날씨 API를 10분 TTL 캐시"
  },
  {
    title: "국토교통부 아파트 매매 실거래가 상세",
    desc: "법정동코드 + 계약년월 기반 조회. 검색 트래픽 테스트용.",
    link: "https://www.data.go.kr/tcs/dss/selectDataSetList.do?keyword=%EC%8B%A4%EA%B1%B0%EB%9E%98%EA%B0%80",
    useCase: "조건검색 결과를 Redis Hash로 캐시"
  },
  {
    title: "한국관광공사 지역별 관광 자원 수요",
    desc: "지역 관광 수요지수 제공. 대시보드/랭킹 실습용.",
    link: "https://www.data.go.kr/data/15152138/openapi.do",
    useCase: "Sorted Set으로 지역 랭킹 구성"
  }
];

const grid = document.getElementById("dataset-grid");

for (const ds of datasets) {
  const card = document.createElement("article");
  card.className = "dataset";
  card.innerHTML = `
    <h3>${ds.title}</h3>
    <p>${ds.desc}</p>
    <p><strong>실습 아이디어:</strong> ${ds.useCase}</p>
    <a href="${ds.link}" target="_blank" rel="noreferrer">데이터셋 보기</a>
  `;
  grid.appendChild(card);
}

for (const button of document.querySelectorAll("[data-copy]")) {
  button.addEventListener("click", async () => {
    try {
      await navigator.clipboard.writeText(button.dataset.copy);
      const prev = button.textContent;
      button.textContent = "복사됨";
      setTimeout(() => {
        button.textContent = prev;
      }, 1200);
    } catch (_) {
      button.textContent = "복사 실패";
    }
  });
}
